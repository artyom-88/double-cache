package com.ganev.doublecache.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main cache interface
 *
 * @param <K> key type for association in this cache
 * @param <V> value type for association with key in this cache
 */
public interface Cache<K, V> {

  /**
   * Associates {@code key} with {@code value} in this cache
   *
   * @param key key for association
   * @param value value for association. If the cache previously contained a value associated with
   *     key, the old value is replaced by new
   */
  void put(K key, V value);

  /**
   * Copies all {@code map} entries to cache
   *
   * @param map map for copying to the cache
   */
  void putAll(Map<? extends K, ? extends V> map);

  /**
   * Returns {@code value} associated with {@code key} in this cache
   *
   * @param key key
   * @return value
   */
  V get(K key) throws IOException, ClassNotFoundException;

  /**
   * Removes cache entry by key
   *
   * @param key key
   */
  V remove(K key);

  /** Removes all entries from cache */
  void clear();

  /**
   * Checks if cache contains key
   *
   * @param key key
   * @return true if cache contains key, else false
   */
  boolean contains(K key);

  /**
   * Returns number of entries in this cache
   *
   * @return number of entries
   */
  long size();

  /**
   * Returns {@code value} call frequency by {@code key}
   *
   * @param key cache entry key
   * @return value call frequency
   */
  int getFrequency(K key);

  /**
   * Returns list of keys, sorted by associated with this key value frequency
   *
   * @return list of keys associated with values in this cache
   */
  List<K> mostFrequentKeys();

  /** Refreshes existing double level cache by allocating objects between cache levels */
  default void refresh() throws IOException, ClassNotFoundException {
    throw new UnsupportedOperationException();
  }
}
