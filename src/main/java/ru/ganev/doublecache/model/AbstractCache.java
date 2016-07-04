package ru.ganev.doublecache.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final Map<K, FrequencyContainer<V>> hash = new HashMap<>();

    @Override
    public abstract void put(K key, V value);

    @Override
    public abstract void putAll(Map<? extends K, ? extends V> m);

    @Override
    public abstract V get(K key) throws IllegalAccessException;

    @Override
    public Map<K, V> getAll() {
        if (hash.isEmpty()) {
            return Collections.emptyMap();
        } else {
            return hash.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getObject()));
        }
    }

    @Override
    public abstract void clear();

    @Override
    public abstract void remove(K key);

    @Override
    public long size() {
        return hash.size();
    }

    public Set<K> mostFrequentKeys() {
        return null;
    }

    public int getFrequency(K key) {
        if (hash.containsKey(key)) {
            return hash.get(key).getFrequency();
        }
        return 0;
    }
}
