package ru.ganev.doublecache.impl;

import java.io.IOException;
import java.util.Map;

import ru.ganev.doublecache.model.DoubleCache;

public class DoubleCacheImpl<K, V> implements DoubleCache<K, V> {

    private final MemoryCache<K, V> memoryCache = new MemoryCache<>();
    private final FileCache<K, V> fileCache;
    private int memCacheCapacity;
    private int requestsAmount;
    private int maxRequestsAmount;

    public DoubleCacheImpl(final int memCacheCapacity, final int maxRequestsAmount, FileCache<K, V> fileCache) {
        this.memCacheCapacity = memCacheCapacity;
        this.requestsAmount = 0;
        this.maxRequestsAmount = maxRequestsAmount;
        this.fileCache = fileCache;
    }

    public static DoubleCacheImplBuilder builder() {
        return new DoubleCacheImplBuilder();
    }

    @Override
    public void put(K key, V value) {
        memoryCache.put(key, value);
    }

    @Override
    public V get(K key) throws IllegalAccessException, IOException, ClassNotFoundException {
        V value;
        if (memoryCache.contains(key)) {
            value = memoryCache.get(key);
        } else {
            value = fileCache.get(key);
        }
        if (++requestsAmount > maxRequestsAmount) {
            this.recache();
            requestsAmount = 0;
        }
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void remove(K key) {
        if (memoryCache.contains(key)) {
            memoryCache.remove(key);
        }
        if (fileCache.contains(key)) {
            fileCache.remove(key);
        }
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
        return 0;
    }

    @Override
    public void recache() throws IOException, ClassNotFoundException {

    }

    public static class DoubleCacheImplBuilder<K, V> {

        private String fileCachePath;
        private int memCacheCapacity;
        private int requestsAmount;

        private DoubleCacheImplBuilder() {
        }

        public DoubleCacheImplBuilder fileCachePath(String fileCachePath) {
            this.fileCachePath = fileCachePath;
            return this;
        }

        public DoubleCacheImplBuilder memCacheCapacity(int memCacheCapacity) {
            this.memCacheCapacity = memCacheCapacity;
            return this;
        }

        public DoubleCacheImplBuilder requestsAmount(int requestsAmount) {
            this.requestsAmount = requestsAmount;
            return this;
        }

        public DoubleCache build() {
            FileCache<K, V> cache;
            if (fileCachePath != null) {
                cache = new FileCache<>(fileCachePath);
            } else {
                cache = new FileCache<>();
            }
            return new DoubleCacheImpl<>(memCacheCapacity, requestsAmount, cache);
        }
    }
}
