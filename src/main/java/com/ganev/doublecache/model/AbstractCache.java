package com.ganev.doublecache.model;

import com.ganev.doublecache.comparator.MostFrequentKeysComparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract cache class implementing main {@link com.ganev.doublecache.model.Cache} interface
 * methods. Requires from subclasses implementation of methods {@link #put} & {@link #get}
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

  protected final ConcurrentMap<K, FrequencyContainer<V>> frequencyMap = new ConcurrentHashMap<>();

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
    FrequencyContainer<V> container = frequencyMap.get(key);
    if (container == null) {
      return null;
    }
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
    return Optional.ofNullable(frequencyMap.get(key))
        .map(FrequencyContainer::getFrequency)
        .orElse(0);
  }

  @Override
  public final List<K> mostFrequentKeys() {
    return frequencyMap.keySet().stream()
        .sorted(new MostFrequentKeysComparator<>(frequencyMap))
        .toList();
  }
}
