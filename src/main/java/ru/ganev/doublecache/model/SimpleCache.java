package ru.ganev.doublecache.model;

import java.util.Map;

public class SimpleCache<K, V> extends AbstractCache<K, V> {

    @Override
    public void put(K key, V value) {
        hash.put(key, value);
        freqMap.put(key, 1);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        hash.putAll(m);
    }

    @Override
    public V get(K key) throws IllegalAccessException {
        if (hash.containsKey(key)) {
            int f = freqMap.get(key);
            freqMap.put(key, ++f);
            return hash.get(key);
        } else {
            throw new IllegalAccessException(String.format("Key %s doesn't exist", key));
        }
    }

    @Override
    public void clear() {
        hash.clear();
        freqMap.clear();
    }

    @Override
    public void remove(K key) {
        hash.remove(key);
        freqMap.remove(key);
    }
}
