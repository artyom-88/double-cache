package com.ganev.doublecache.impl;

import com.ganev.doublecache.model.Cache;
import com.ganev.doublecache.model.RequestCounter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Double level cache for storing objects in RAM and file system
 *
 * @param <K> key type
 * @param <V> value type
 */
public class DoubleCache<K, V> implements Cache<K, V> {

  private final MemoryCache<K, V> memoryCache = new MemoryCache<>();
  private final FileCache<K, V> fileCache;
  private final int memCacheSize;
  private final RequestCounter requestCounter;

  public DoubleCache(final int memCacheSize, final int maxRequestsAmount) {
    this(memCacheSize, maxRequestsAmount, null);
  }

  public DoubleCache(final int memCacheSize, final int maxRequestsAmount, String fileCachePath) {
    this.memCacheSize = memCacheSize;
    this.fileCache = fileCachePath == null ? new FileCache<>() : new FileCache<>(fileCachePath);
    this.requestCounter = new RequestCounter(maxRequestsAmount);
  }

  @Override
  public void put(K key, V value) {
    if (memoryCache.size() < memCacheSize) {
      memoryCache.put(key, value);
    } else {
      fileCache.put(key, value);
    }
  }

  @Override
  public V get(K key) throws IOException, ClassNotFoundException {
    requestCounter.increment();
    if (requestCounter.hasReachedMaxRequests()) {
      this.refresh();
      requestCounter.reset();
    }
    return memoryCache.contains(key) ? memoryCache.get(key) : fileCache.get(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    map.forEach(this::put);
  }

  @Override
  public V remove(K key) {
    if (memoryCache.contains(key)) {
      return memoryCache.remove(key);
    }
    if (fileCache.contains(key)) {
      return fileCache.remove(key);
    }
    return null;
  }

  @Override
  public void clear() {
    memoryCache.clear();
    fileCache.clear();
  }

  @Override
  public boolean contains(K key) {
    return memoryCache.contains(key) || fileCache.contains(key);
  }

  @Override
  public long size() {
    return memoryCache.size() + fileCache.size();
  }

  @Override
  public int getFrequency(K key) {
    if (memoryCache.contains(key)) {
      return memoryCache.getFrequency(key);
    }
    if (fileCache.contains(key)) {
      return fileCache.getFrequency(key);
    }
    return 0;
  }

  @Override
  public List<K> mostFrequentKeys() {
    List<K> allKeys = this.mergeKeys(memoryCache.mostFrequentKeys(), fileCache.mostFrequentKeys());
    return this.sortKeysByFrequency(allKeys);
  }

  @Override
  public void refresh() throws IOException, ClassNotFoundException {
    int averageFrequency = getAverageFrequency();
    this.moveLowFrequencyKeysFromMemoryToFile(averageFrequency);
    this.moveHighFrequencyKeysFromFileToMemory(averageFrequency);
    this.moveRemainingKeysFromFileToMemory();
  }

  private List<K> mergeKeys(List<K> keys1, List<K> keys2) {
    List<K> mergedKeys = new ArrayList<>(keys1);
    mergedKeys.addAll(keys2);
    return mergedKeys;
  }

  private List<K> sortKeysByFrequency(List<K> keys) {
    return keys.stream()
        .sorted((o1, o2) -> Integer.compare(this.getFrequency(o2), this.getFrequency(o1)))
        .collect(Collectors.toList());
  }

  private int getAverageFrequency() {
    List<K> keys = this.mostFrequentKeys();
    if (keys.isEmpty()) return 0;
    int result = keys.stream().map(this::getFrequency).reduce(Integer::sum).orElse(0);
    return result / keys.size();
  }

  private boolean isLowFrequencyKey(K key, int averageFrequency) {
    return memoryCache.getFrequency(key) < averageFrequency;
  }

  private void moveLowFrequencyKeysFromMemoryToFile(int averageFrequency) {
    List<K> memKeys = memoryCache.mostFrequentKeys();
    memKeys.stream()
        .filter(key -> this.isLowFrequencyKey(key, averageFrequency))
        .forEach(key -> fileCache.put(key, memoryCache.remove(key)));
  }

  private boolean isHighFrequencyKeyFromFileToMemory(K key, int averageFrequency) {
    return fileCache.getFrequency(key) >= averageFrequency && memoryCache.size() < memCacheSize;
  }

  private void moveHighFrequencyKeysFromFileToMemory(int averageFrequency) {
    List<K> fileKeys = fileCache.mostFrequentKeys();
    fileKeys.stream()
        .filter(key -> isHighFrequencyKeyFromFileToMemory(key, averageFrequency))
        .forEach(key -> memoryCache.put(key, fileCache.getFrequency(key), fileCache.remove(key)));
  }

  private boolean isMemoryCacheBelowCapacity() {
    return memoryCache.size() < memCacheSize;
  }

  private void moveRemainingKeysFromFileToMemory() {
    fileCache.mostFrequentKeys().stream()
        .filter(key -> this.isMemoryCacheBelowCapacity())
        .forEach(key -> memoryCache.put(key, fileCache.remove(key)));
  }
}
