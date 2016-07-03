package ru.ganev.doublecache.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractCache<K, V> implements Cache<K, V>, Frequency<K> {

    protected final Map<K, V> hash = new HashMap<>();
    protected final Map<K, Integer> freqMap = new HashMap<>();

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
            return hash;
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

    @Override
    public Set<K> mostFrequentKeys() {
        return freqMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public int getFrequency(K key) {
        if (hash.containsKey(key)) {
            return freqMap.get(key);
        }
        return 0;
    }
}
