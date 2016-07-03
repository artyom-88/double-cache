package ru.ganev.doublecache.model;

import java.io.File;
import java.util.Map;

public class FileCache<K, V> extends AbstractCache<K, V> {

    public static final String DEFAULT_CACHE_PATH = "./tmp/";

    public FileCache() {
        File tmpDir = new File(DEFAULT_CACHE_PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public V get(K key) throws IllegalAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(K key) {

    }
}
