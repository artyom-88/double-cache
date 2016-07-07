package ru.ganev.doublecache.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.ganev.doublecache.model.DoubleCache;

/**
 * Implementation of {@link ru.ganev.doublecache.model.DoubleCache}
 *
 * @param <K> key type
 * @param <V> value type
 */
public class DoubleCacheImpl<K, V> implements DoubleCache<K, V> {

    private final MemoryCache<K, V> memoryCache = new MemoryCache<>();
    private FileCache<K, V> fileCache;
    private int memCacheSize;
    private int requestsAmount;
    private int maxRequestsAmount;

    public DoubleCacheImpl(final int memCacheSize, final int maxRequestsAmount) {
        initFields(memCacheSize, maxRequestsAmount, null);
    }

    public DoubleCacheImpl(final int memCacheSize, final int maxRequestsAmount, String fileCachePath) {
        initFields(memCacheSize, maxRequestsAmount, fileCachePath);
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
        return list.stream()
                .sorted((o1, o2) -> {
                    if (this.getFrequency(o1) <= this.getFrequency(o2)) {
                        return 1;
                    }
                    return -1;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void refresh() throws IOException, ClassNotFoundException {
        List<K> memKeys = memoryCache.mostFrequentKeys();
        int avFrequency = getAvaregeFrequency(memKeys);
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

    private void initFields(final int memCacheSize, final int maxRequestsAmount, String path) {
        this.memCacheSize = memCacheSize;
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
        result += keys.stream()
                .map(fileCache::getFrequency)
                .reduce((i1, i2) -> i1 + i2)
                .orElse(0);
        return keys.size() == 0 ? 0 : result / keys.size();
    }
}
