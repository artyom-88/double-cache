package ru.ganev.doublecache;

import java.util.Map;

/**
 * Main interface
 */
public interface Cache<K, V> {

    /**
     * @param key   key for association in this cache
     * @param value value for association with key in this cache. If the cache previously contained a
     *              value associated with key, the old value is replaced by
     */
    void put(K key, V value);

    /**
     * @param m hash for copying to the cache
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * @param key key
     * @return value associated with key in this cache
     * @throws IllegalAccessException
     */
    V get(K key) throws IllegalAccessException;

    /**
     * removes all entries from cache
     */
    void clear();

    /**
     * @param key removes cache entry by key
     */
    void remove(K key);

    /**
     * @return number of entries in this cache
     */
    long size();

}
