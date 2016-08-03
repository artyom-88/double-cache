package ru.ganev.doublecache.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Abstract cache class implementing main {@link ru.ganev.doublecache.model.Cache} interface methods. Requires from
 * subclasses implementation of methods {@link #put} & {@link #get}
 *
 * @param <K> key type
 * @param <V> value type
 */

public abstract class AbstractCache<K, V> implements Cache<K, V> {

    protected final Map<K, FrequencyContainer<V>> hash = new ConcurrentHashMap<>();

    @Override
    public abstract void put(K key, V value);

    @Override
    public abstract V get(K key);

    @Override
    public final void putAll(Map<? extends K, ? extends V> map) {
        map.entrySet().stream()
                .forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public V remove(K key) {
        return hash.remove(key).getObject();
    }

    @Override
    public void clear() {
        hash.clear();
    }

    @Override
    public final boolean contains(K key) {
        return hash.containsKey(key);
    }

    @Override
    public final long size() {
        return hash.size();
    }

    @Override
    public final int getFrequency(K key) {
        if (this.contains(key)) {
            return hash.get(key).getFrequency();
        }
        return 0;
    }

    @Override
    public final List<K> mostFrequentKeys() {
        Set<K> keys = hash.keySet();
        return keys.stream()
                .sorted((o1, o2) -> {
                    if (hash.get(o1).getFrequency() <= hash.get(o2).getFrequency()) {
                        return 1;
                    }
                    return -1;
                })
                .collect(Collectors.toList());
    }
}
