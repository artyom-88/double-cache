package com.ganev.doublecache.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestCache {

  @Test
  public void testDefaultRefreshThrows() {
    Cache<String, String> cache =
        new Cache<>() {
          @Override
          public void put(String key, String value) {}

          @Override
          public void putAll(Map<? extends String, ? extends String> map) {}

          @Override
          public String get(String key) throws IOException, ClassNotFoundException {
            return "";
          }

          @Override
          public String remove(String key) {
            return "";
          }

          @Override
          public void clear() {}

          @Override
          public boolean contains(String key) {
            return false;
          }

          @Override
          public long size() {
            return 0;
          }

          @Override
          public int getFrequency(String key) {
            return 0;
          }

          @Override
          public List<String> mostFrequentKeys() {
            return List.of();
          }
        };
    assertThrows(UnsupportedOperationException.class, cache::refresh);
  }
}
