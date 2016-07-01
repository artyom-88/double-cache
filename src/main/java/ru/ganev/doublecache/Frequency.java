package ru.ganev.doublecache;

import java.util.Set;

public interface Frequency<K> {

    Set<K> mostFrequentlyKeys();

    int getFrequency(K key);

}
