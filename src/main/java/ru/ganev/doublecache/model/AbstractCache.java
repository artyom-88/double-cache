package ru.ganev.doublecache.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final Map<K, FrequencyContainer<V>> hash = new HashMap<>();

    @Override
    public abstract void put(K key, V value);

    @Override
    public abstract V get(K key) throws IllegalAccessException;

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.entrySet().stream()
                .forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void remove(K key) {
        hash.remove(key);
    }

    @Override
    public void clear() {
        hash.clear();
    }

    @Override
    public boolean contains(K key) {
        return hash.containsKey(key);
    }

    @Override
    public final long size() {
        return hash.size();
    }

    //TODO: return set of keys sorted by frequency
    public Set<K> mostFrequentKeys() {
        return null;
    }

    @Override
    public int getFrequency(K key) {
        if (this.contains(key)) {
            return hash.get(key).getFrequency();
        }
        return 0;
    }
}
