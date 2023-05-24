package com.huang.lightweight.server.registry.util;

import com.huang.lightweight.common.pojo.Instance;
import com.huang.lightweight.common.util.cache.JvmCachePool;
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


    /**
     * Get the singleton instance of InstanceCachePool
     *
     * @return The instance of InstanceCachePool
     */

    /**
     * Put a key-value pair into the cache pool
     *
     * @param key   The key to associate the value with
     * @param value The value to be stored in the cache pool
     * @return ture/false
     */
    public synchronized void put(String key, Instance value) {
        if (cache.containsKey(key)) {
            // check old value
            List<Instance> list = cache.get(key);
            boolean isExit = false;
            for (int i = 0; i < list.size(); i++) {
                Instance instance = list.get(i);
                if (instance.getIp().equals(value.getIp()) && instance.getPort() == value.getPort()) {
                    list.set(i, value);
                    isExit = true;
                    break;
                }
            }
            // value is exit -> update
            if (isExit) {
                cache.put(key, list);
            } else {
                cache.get(key).add(value);
            }

        } else {
            ArrayList<Instance> tem = new ArrayList<>();
            tem.add(value);
            cache.put(key, tem);
        }
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
