package ru.ganev.doublecache.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ru.ganev.doublecache.model.DoubleCache;

public class DoubleCacheImpl<K, V> implements DoubleCache<K, V> {

    private final MemoryCache<K, V> memoryCache = new MemoryCache<>();
    private FileCache<K, V> fileCache;
    private int memCacheCapacity;
    private int requestsAmount;
    private int maxRequestsAmount;

    public DoubleCacheImpl(final int memCacheCapacity, final int maxRequestsAmount) {
        initFields(memCacheCapacity, maxRequestsAmount, null);
    }

    public DoubleCacheImpl(final int memCacheCapacity, final int maxRequestsAmount, String fileCachePath) {
        initFields(memCacheCapacity, maxRequestsAmount, fileCachePath);
    }

    @Override
    public void put(K key, V value) {
        memoryCache.put(key, value);
    }

    @Override
    public V get(K key) throws IOException, ClassNotFoundException {
        V value;
        if (memoryCache.contains(key)) {
            value = memoryCache.get(key);
        } else {
            value = fileCache.get(key);
        }
        if (++requestsAmount > maxRequestsAmount) {
            this.refresh();
            requestsAmount = 0;
        }
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.entrySet().stream()
                .forEach(entry -> this.put(entry.getKey(), entry.getValue()));
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
        if (memoryCache.contains(key)) {
            return true;
        }
        if (fileCache.contains(key)) {
            return true;
        }
        return false;
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
        return list;
    }

    @Override
    public void refresh() throws IOException, ClassNotFoundException {
        List<K> memKeySet = memoryCache.mostFrequentKeys();
        int avFrequency = getAvaregeFrequency(memKeySet);
        memKeySet.stream()
                .filter(key -> memoryCache.getFrequency(key) <= avFrequency)
                .peek(key -> fileCache.put(key, memoryCache.get(key)))
                .forEach(memoryCache::remove);
        List<K> fileKeySet = fileCache.mostFrequentKeys();
        fileKeySet.stream()
                .filter(key -> fileCache.getFrequency(key) > avFrequency)
                .peek(key -> memoryCache.put(key, fileCache.get(key)))
                .forEach(fileCache::remove);
    }

    private void initFields(final int memCacheCapacity, final int maxRequestsAmount, String path) {
        this.memCacheCapacity = memCacheCapacity;
        this.requestsAmount = 0;
        this.maxRequestsAmount = maxRequestsAmount;
        if (path == null) {
            this.fileCache = new FileCache<>();
        } else {
            this.fileCache = new FileCache<>(path);
        }
    }

    private int getAvaregeFrequency(List<K> keys) {
        int result = keys.stream()
                .map(memoryCache::getFrequency)
                .reduce((i1, i2) -> i1 + i2)
                .orElse(0);
        return result / keys.size();
    }
}
