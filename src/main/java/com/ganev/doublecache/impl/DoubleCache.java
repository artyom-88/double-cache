package com.ganev.doublecache.impl;

import com.ganev.doublecache.model.Cache;

import java.io.IOException;
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
    private int requestsAmount;
    private final int maxRequestsAmount;

    public DoubleCache(final int memCacheSize, final int maxRequestsAmount) {
        this(memCacheSize, maxRequestsAmount, null);
    }

    public DoubleCache(final int memCacheSize, final int maxRequestsAmount, String fileCachePath) {
        this.memCacheSize = memCacheSize;
        this.requestsAmount = 0;
        this.maxRequestsAmount = maxRequestsAmount;
        this.fileCache = fileCachePath == null ? new FileCache<>() : new FileCache<>(fileCachePath);
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
        if (++requestsAmount > maxRequestsAmount) {
            this.refresh();
            requestsAmount = 0;
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
        List<K> list = memoryCache.mostFrequentKeys();
        list.addAll(fileCache.mostFrequentKeys());
        return list.stream()
                .sorted((o1, o2) -> Integer.compare(this.getFrequency(o2), this.getFrequency(o1)))
                .collect(Collectors.toList());
    }

    @Override
    public void refresh() throws IOException, ClassNotFoundException {
        int avFrequency = getAverageFrequency();
        List<K> memKeys = memoryCache.mostFrequentKeys();
        memKeys.stream()
                .filter(key -> memoryCache.getFrequency(key) < avFrequency)
                .forEach(key -> fileCache.put(key, memoryCache.remove(key)));
        List<K> fileKeys = fileCache.mostFrequentKeys();
        fileKeys.stream()
                .filter(key -> fileCache.getFrequency(key) >= avFrequency && memoryCache.size() < memCacheSize)
                .forEach(key -> memoryCache.put(key, fileCache.getFrequency(key), fileCache.remove(key)));
        fileCache.mostFrequentKeys().stream()
                .filter(key -> memoryCache.size() < memCacheSize)
                .forEach(key -> memoryCache.put(key, fileCache.remove(key)));
    }

    private int getAverageFrequency() {
        List<K> keys = this.mostFrequentKeys();
        if (keys.isEmpty()) return 0;
        int result = keys.stream()
                .map(this::getFrequency)
                .reduce(Integer::sum)
                .orElse(0);
        return result / keys.size();
    }
}
