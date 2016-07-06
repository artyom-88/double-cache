package ru.ganev.doublecache.model;

import java.io.IOException;
import java.util.List;
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
     * @param map map for copying to the cache
     */
    void putAll(Map<? extends K, ? extends V> map);

    /**
     * @param key key
     * @return value associated with key in this cache
     */
    V get(K key) throws IOException, ClassNotFoundException;

    /**
     * @param key removes cache entry by key
     */
    V remove(K key);

    /**
     * removes all entries from cache
     */
    void clear();

    /**
     * @param key associated key
     * @return true if cache contains key, else false
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

    List<K> mostFrequentKeys();

}
