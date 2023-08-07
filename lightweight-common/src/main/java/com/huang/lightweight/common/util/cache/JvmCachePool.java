package com.huang.lightweight.common.util.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JVM Cache Pool
 * @param <K> The type of keys
 * @param <V> The type of values
 */
public class JvmCachePool<K, V> {
    private final Map<K, V> cache;

    /**
     * Constructor, initializes the cache pool.
     */
    public JvmCachePool() {
        cache = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     * @param key The key to retrieve the value for
     * @return The value associated with the key, or null if the key is not found in the cache
     */
    public V get(K key) {
        return cache.get(key);
    }

    /**
     * Adds or updates a key-value pair in the cache.
     * @param key The key to add or update
     * @param value The value to associate with the key
     */
    public void put(K key, V value) {
        cache.put(key, value);
    }

    /**
     * Removes the key-value pair associated with the specified key from the cache.
     * @param key The key to remove from the cache
     */
    public void remove(K key) {
        cache.remove(key);
    }

    /**
     * Clears the cache, removing all key-value pairs.
     */
    public void clear() {
        cache.clear();
    }

    /**
     * Checks if the cache contains the specified key.
     * @param key The key to check for
     * @return true if the cache contains the key, false otherwise
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * Returns the values in the cache as a List.
     *
     * @return The values in the cache as a List
     */
    public List<V> getValuesAsList() {
        return new ArrayList<>(cache.values());
    }
}