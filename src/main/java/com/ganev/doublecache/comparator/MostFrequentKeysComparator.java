package com.ganev.doublecache.comparator;

import com.ganev.doublecache.model.FrequencyContainer;
import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;

public class MostFrequentKeysComparator<K, V> implements Comparator<K> {

  private final ConcurrentMap<K, FrequencyContainer<V>> frequencyMap;

  public MostFrequentKeysComparator(ConcurrentMap<K, FrequencyContainer<V>> frequencyMap) {
    this.frequencyMap = frequencyMap;
  }

  @Override
  public int compare(K key1, K key2) {
    return Integer.compare(
        frequencyMap.get(key2).getFrequency(), frequencyMap.get(key1).getFrequency());
  }
}
