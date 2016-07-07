package ru.ganev.doublecache.model;

import java.io.IOException;

/**
 * Main interface for double level caches (RAM and Files)
 *
 * @param <K> key type for association in this cache
 * @param <V> value type for association with key in this cache
 */
public interface DoubleCache<K, V> extends Cache<K, V> {

    /**
     * Refreshes existing double level cache by allocating objects between cache levels
     */
    void refresh() throws IOException, ClassNotFoundException;

}
