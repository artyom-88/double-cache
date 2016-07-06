package ru.ganev.doublecache.model;

import java.io.IOException;

public interface DoubleCache<K, V> extends Cache<K, V> {

    void refresh() throws IOException, ClassNotFoundException, IllegalAccessException;

}
