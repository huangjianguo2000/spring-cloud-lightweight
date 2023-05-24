package com.huang.lightweight.server.registry.util;

import com.huang.lightweight.common.exception.LightWeightException;
import com.huang.lightweight.common.model.v1.ErrorCode;
import com.huang.lightweight.common.pojo.instance.Instance;
import com.huang.lightweight.common.util.cache.JvmCachePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance.
 *
 * @author lightweight
 */
@Component
public class InstanceCachePool {

    private final JvmCachePool<String, List<Instance>> cache = new JvmCachePool<>();


    @Autowired
    private ScheduledThreadPool scheduledThreadPool;

    /**
     * Put a key-value pair into the cache pool
     *
     * @param key   The key to associate the value with
     * @param value The value to be stored in the cache pool
     * @return ture/false already had one
     */
    public synchronized void put(String key, Instance value) throws LightWeightException {
        if (cache.containsKey(key)) {
            // check old value
            List<Instance> list = cache.get(key);
            for (int i = 0; i < list.size(); i++) {
                Instance instance = list.get(i);
                if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                    throw new LightWeightException(ErrorCode.SERVER_INSTANCE_EXIST,
                            value.getIp() + ":" + value.getPort() + ", instance is already exist");
                }
            }
            // add ..
            cache.get(key).add(value);
        } else {
            // new and add ..
            ArrayList<Instance> tem = new ArrayList<>();
            tem.add(value);
            cache.put(key, tem);
        }

        // heart beat
        scheduledThreadPool.execute(value, cache);
    }

    /**
     * update a key-value pair into the cache pool
     *
     * @param key   The key to associate the value with
     * @param value The value to be stored in the cache pool
     * @return ture/false not exit
     */
    public synchronized void update(String key, Instance value) throws LightWeightException {
        List<Instance> list = cache.get(key);
        for (int i = 0; i < list.size(); i++) {
            Instance instance = list.get(i);
            if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                list.set(i, value);
                return;
            }
        }
        throw new LightWeightException(ErrorCode.SERVER_INSTANCE_NOT_EXIST,
                value.getIp() + ":" + value.getPort() + ", instance not exist");
    }

    /**
     * Get the value associated with the specified key from the cache pool
     *
     * @param key The key to retrieve the value
     * @return The value associated with the key, or null if the key is not found
     */
    public synchronized List<Instance> get(String key) {
        return cache.get(key);
    }

    /**
     * list all instance
     *
     * @return instances list
     */
    public synchronized List<List<Instance>> list() {
        return cache.getValuesAsList();
    }

    /**
     * Remove the key-value pair associated with the specified key from the cache pool
     *
     * @param key The key to remove the value
     */
    public synchronized void remove(String key) {
        cache.remove(key);
    }

    /**
     * Clear all key-value pairs from the cache pool
     */
    public synchronized void clear() {
        cache.clear();
    }
}
