package com.ganev.doublecache.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Abstract cache class implementing main {@link com.ganev.doublecache.model.Cache} interface methods. Requires from
 * subclasses implementation of methods {@link #put} & {@link #get}
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final Map<K, FrequencyContainer<V>> frequencyMap = new ConcurrentHashMap<>();

    @Override
    public abstract void put(K key, V value);

    @Override
    public abstract V get(K key);

    @Override
    public final void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    @Override
    public V remove(K key) {
        return frequencyMap.remove(key).getObject();
    }

    @Override
    public void clear() {
        frequencyMap.clear();
    }

    @Override
    public final boolean contains(K key) {
        return frequencyMap.containsKey(key);
    }

    @Override
    public final long size() {
        return frequencyMap.size();
    }

    @Override
    public final int getFrequency(K key) {
        if (this.contains(key)) {
            return frequencyMap.get(key).getFrequency();
        }
        return 0;
    }

    @Override
    public final List<K> mostFrequentKeys() {
        Set<K> keys = frequencyMap.keySet();
        return keys.stream()
                .sorted((o1, o2) -> Integer.compare(frequencyMap.get(o2).getFrequency(), frequencyMap.get(o1).getFrequency()))
                .collect(Collectors.toList());
    }
}
