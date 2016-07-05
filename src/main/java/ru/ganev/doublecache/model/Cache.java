package ru.ganev.doublecache.model;

import java.io.IOException;
import java.util.Map;

/**
 * Main interface for all cache types
 */
public interface Cache<K, V> {

    /**
     * @param key   key for association in this cache
     * @param value value for association with key in this cache. If the cache previously contained a
     *              value associated with key, the old value is replaced by
     */
    void put(K key, V value);

    /**
     * @param m map for copying to the cache
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * @param key key
     * @return value associated with key in this cache
     * @throws IllegalAccessException when key doesn't exist
     */
    V get(K key) throws IllegalAccessException, IOException, ClassNotFoundException;

    /**
     * @param key removes cache entry by key
     */
    void remove(K key);

    /**
     * removes all entries from cache
     */
    void clear();

    /**
     * @param key
     * @return
     */
    boolean contains(K key);

    /**
     * @return number of entries in this cache
     */
    long size();

    /**
     * @param key cache entry key
     * @return value call frequency
     */
    int getFrequency(K key);

}
