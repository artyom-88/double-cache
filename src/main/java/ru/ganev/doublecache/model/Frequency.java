package ru.ganev.doublecache.model;

import java.util.Set;

public interface Frequency<K> {

    Set<K> mostFrequentKeys();

    int getFrequency(K key);

}
