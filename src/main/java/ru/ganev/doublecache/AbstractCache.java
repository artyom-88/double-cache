package ru.ganev.doublecache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class AbstractCache<K, V> implements Cache<K, V>, Frequency<K> {

    protected final Map<K, V> hash = new HashMap<>();
    protected final Map<K, Integer> freqMap = new TreeMap<K, Integer>();

    @Override
    public abstract void put(K key, V value);

    @Override
    public abstract void putAll(Map<? extends K, ? extends V> m);

    @Override
    public abstract V get(K key) throws IllegalAccessException;

    @Override
    public abstract void clear();

    @Override
    public abstract void remove(K key);

    @Override
    public long size() {
        return hash.size();
    }

    @Override
    public Set<K> mostFrequentlyKeys() {
//        freqMap.entrySet().stream().sorted().collect(Collectors.toMap(entry -> ));
        return null;
    }

    @Override
    public int getFrequency(K key) {
        if (hash.containsKey(key)) {
            return freqMap.get(key);
        }
        return 0;
    }
}
