package ru.ganev.doublecache.impl;

import ru.ganev.doublecache.model.AbstractCache;
import ru.ganev.doublecache.model.FrequencyContainer;

/**
 * Simple cache for storing objects in RAM
 *
 * @param <K> key
 * @param <V> object type
 */
public class MemoryCache<K, V> extends AbstractCache<K, V> {

    @Override
    public void put(K key, V value) {
        hash.put(key, new FrequencyContainer<>(value));
    }

    @Override
    public V get(K key) throws IllegalAccessException {
        if (this.contains(key)) {
            return hash.get(key).getObject();
        } else {
            throw new IllegalAccessException(String.format("Key %s doesn't exist", key));
        }
    }
}
