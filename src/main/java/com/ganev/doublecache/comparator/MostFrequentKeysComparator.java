package com.ganev.doublecache.comparator;

import com.ganev.doublecache.model.FrequencyContainer;
import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;

/**
 * Comparator for sorting keys by their associated frequency in descending order. If a key is
 * missing from the frequency map, it is treated as having frequency 0.
 *
 * @param <K> key type
 */
public class MostFrequentKeysComparator<K, V> implements Comparator<K> {

  private final ConcurrentMap<K, FrequencyContainer<V>> frequencyMap;

  public MostFrequentKeysComparator(ConcurrentMap<K, FrequencyContainer<V>> frequencyMap) {
    this.frequencyMap = frequencyMap;
  }

  @Override
  public int compare(K key1, K key2) {
    int freq1 = getFrequency(key1);
    int freq2 = getFrequency(key2);
    return Integer.compare(freq2, freq1);
  }

  private int getFrequency(K key) {
    FrequencyContainer<?> container = frequencyMap.get(key);
    return container == null ? 0 : container.getFrequency();
  }
}
