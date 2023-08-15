package com.huang.lightweight.server.registry;

import com.huang.lightweight.common.exception.LightweightException;
import com.huang.lightweight.common.pojo.InstanceWrapper;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.cache.JvmCachePool;
import com.huang.lightweight.common.util.common.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 服务管理类
 *
 * @Author lightweight
 */
@Component
public class ServiceManager {

    private Logger logger = LoggerFactory.getLogger(ServiceManager.class);

    protected JvmCachePool<String, List<Instance>> cache = new JvmCachePool<>();


    /**
     * 将键值对放入缓存池
     */
    public synchronized void put(String key, Instance value) {
        if (cache.containsKey(key)) {
            // 检查旧值
            List<Instance> list = cache.get(key);
            for (int i = 0; i < list.size(); i++) {
                Instance instance = list.get(i);
                if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                    return;
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
     * 检测实例是否存在
     */
    public boolean checkHasInMap(Instance instance) {
        List<Instance> list = cache.get(instance.getServiceName());
        if(list == null){
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            Instance temp = list.get(i);
            if (instance.getIp().equals(temp.getIp()) && instance.getPort() == temp.getPort()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新缓存池中的键值对
     *
     * @param key   要关联值的键
     * @param value 要更新的值
     * @throws LightweightException 若实例不存在则抛出异常
     */
    public synchronized void update(String key, Instance value) {
        List<Instance> list = cache.get(key);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Instance instance = list.get(i);
                if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                    list.set(i, value);
                    //  cache.put(key, list);
                    return;
                }
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
     */
    public synchronized Instance getInstance(Instance instance) {
        Optional<Instance> instanceOptional = cache.get(instance.getServiceName()).stream()
                .filter(o -> o.getIp().equals(instance.getIp()) && o.getPort() == instance.getPort())
                .findFirst();
        return instanceOptional.orElse(null);
    }

    /**
     * 列出所有实例
     */
    public synchronized List<List<Instance>> list() {
        return cache.getValuesAsList();
    }

    /**
     * Instance包装成 InstanceWrapper
     */
    public List<InstanceWrapper> listInstanceWrapper() {
        List<InstanceWrapper> ans = new ArrayList<>();
        List<List<Instance>> list = list();
        // wrapper
        for (List<Instance> instances : list) {
            if (instances != null && !instances.isEmpty()) {
                InstanceWrapper instanceWrapper = new InstanceWrapper(instances.get(0).getServiceName(), instances);
                ans.add(instanceWrapper);
            }
        }
        return ans;
    }

    /**
     * 包装成MAP， key 实例地址
     */
    public Map<String, Instance> listAsMap() {
        Map<String, Instance> map = new HashMap<>();
        for (List<Instance> instances : list()) {
            for (Instance instance : instances) {
                map.put(instance.getIp() + instance.getPort(), instance);
            }
        }
        return map;
    }

    /**
     * 从缓存池中移除指定的实例
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
    public void replaceData(Map<String, List<Instance>> data) {
        cache.clear();
        cache.replaceData(data);
    }

}

