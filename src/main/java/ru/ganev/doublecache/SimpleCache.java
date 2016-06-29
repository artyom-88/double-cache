package ru.ganev.doublecache;

import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> implements Cache<K, V> {

    private final Map<K, V> map = new HashMap<>();

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public V get(K key) throws IllegalAccessException {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            throw new IllegalAccessException(String.format("Key %s doesn't exist", key));
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public long size() {
        return map.size();
    }
}
