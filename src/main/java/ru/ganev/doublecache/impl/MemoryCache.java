package ru.ganev.doublecache.impl;

import ru.ganev.doublecache.model.AbstractCache;
import ru.ganev.doublecache.model.FrequencyContainer;

/**
 * Simple cache for storing objects in RAM
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MemoryCache<K, V> extends AbstractCache<K, V> {

    @Override
    public void put(K key, V value) {
        hash.put(key, new FrequencyContainer<>(value));
    }

    @Override
    public V get(K key) {
        if (this.contains(key)) {
            FrequencyContainer<V> container = hash.get(key);
            container.incFrequency();
            return container.getObject();
        }
        return null;
    }

    void put(K key, int frequency, V value) {
        FrequencyContainer<V> container = new FrequencyContainer<>(value, frequency);
        hash.put(key, container);
    }
}
