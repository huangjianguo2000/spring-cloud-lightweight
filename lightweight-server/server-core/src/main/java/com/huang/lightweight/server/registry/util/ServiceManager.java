package com.huang.lightweight.server.registry.util;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.cache.JvmCachePool;
import com.huang.lightweight.common.util.common.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 服务管理类
 *
 * @Author lightweight
 */
@Component
public class ServiceManager {

    private Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    protected final JvmCachePool<String, List<Instance>> cache = new JvmCachePool<>();



    /**
     * 将键值对放入缓存池
     *
     * @param key   要关联值的键
     * @param value 要存储在缓存池中的值
     * @throws LightweightException 若已存在相同的实例则抛出异常
     */
    public synchronized void put(String key, Instance value) throws LightweightException {
        if (cache.containsKey(key)) {
            // 检查旧值
            List<Instance> list = cache.get(key);
            for (int i = 0; i < list.size(); i++) {
                Instance instance = list.get(i);
                if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                    throw new LightweightException(ErrorCode.SERVER_INSTANCE_EXIST,
                            value.getIp() + ":" + value.getPort() + "，instance is already exist");
                }
            }
            // 添加...
            cache.get(key).add(value);
        } else {
            // 新建并添加...
            ArrayList<Instance> tem = new ArrayList<>();
            tem.add(value);
            cache.put(key, tem);
        }

    }

    /**
     * 更新缓存池中的键值对
     *
     * @param key   要关联值的键
     * @param value 要更新的值
     * @throws LightweightException 若实例不存在则抛出异常
     */
    public synchronized void update(String key, Instance value) throws LightweightException {
        List<Instance> list = cache.get(key);
        for (int i = 0; i < list.size(); i++) {
            Instance instance = list.get(i);
            if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                list.set(i, value);
                cache.put(key, list);
                return;
            }
        }
        // 实例不存在
        put(key, value);
    }

    /**
     * 根据键从缓存池中获取关联的值
     *
     * @param key 要检索值的键
     * @return 与键关联的值，若键不存在则返回 null
     */
    public synchronized List<Instance> get(String key) {
        return cache.get(key);
    }

    /**
     * 获取具体的实例对象
     * @param instance 服务实例
     * @return
     */
    public synchronized Instance getInstance(Instance instance) {
        Optional<Instance> instanceOptional = cache.get(instance.getServiceName()).stream()
                .filter(o -> o.getIp().equals(instance.getIp()) && o.getPort() == instance.getPort())
                .findFirst();
        return instanceOptional.orElse(null);
    }

    /**
     * 列出所有实例
     *
     * @return 实例列表
     */
    public synchronized List<List<Instance>> list() {
        return cache.getValuesAsList();
    }

    /**
     * 根据键从缓存池中移除关联的键值对
     *
     * @param key 要移除值的键
     */
    public synchronized void remove(String key) {
        cache.remove(key);
    }

    /**
     * 清空缓存池中的所有键值对
     */
    public synchronized void clear() {
        cache.clear();
    }

    /**
     * 从缓存池中移除指定的实例
     * @param instance 要移除的实例
     */
    public void removeInstance(Instance instance) {
        // 获取与服务名相关的实例列表
        List<Instance> instances = cache.get(instance.getServiceName());
        if (ListUtil.isEmpty(instances)) {
            return;
        }
        // 从列表中移除指定的实例
        instances.remove(instance);

        if (instances.isEmpty()) {
            // 若列表为空，则将服务名从缓存池中移除
            cache.remove(instance.getServiceName());
        } else {
            // 若列表仍包含实例，则使用修改后的列表更新缓存池
            cache.put(instance.getServiceName(), instances);
        }
    }

    /**
     * 替换所有的数据
     */
    public void replaceData(Map<String, List<Instance>> data){
        cache.clear();
        cache.replaceData(data);
    }
}

