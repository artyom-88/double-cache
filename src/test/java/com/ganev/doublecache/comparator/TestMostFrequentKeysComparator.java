package com.ganev.doublecache.comparator;

import static org.junit.jupiter.api.Assertions.*;

import com.ganev.doublecache.model.FrequencyContainer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestMostFrequentKeysComparator {

  private ConcurrentMap<String, FrequencyContainer<String>> frequencyMap;
  private MostFrequentKeysComparator<String, String> comparator;

  @BeforeEach
  void setUp() {
    frequencyMap = new ConcurrentHashMap<>();
    comparator = new MostFrequentKeysComparator<>(frequencyMap);
  }

  @Test
  void testCompareHigherFrequencyFirst() {
    frequencyMap.put("A", new FrequencyContainer<>("A", 5));
    frequencyMap.put("B", new FrequencyContainer<>("B", 2));
    assertTrue(comparator.compare("A", "B") < 0);
    assertTrue(comparator.compare("B", "A") > 0);
  }

  @Test
  void testCompareEqualFrequencies() {
    frequencyMap.put("A", new FrequencyContainer<>("A", 3));
    frequencyMap.put("B", new FrequencyContainer<>("B", 3));
    assertEquals(0, comparator.compare("A", "B"));
  }

  @Test
  void testCompareMissingKeyTreatedAsZero() {
    frequencyMap.put("A", new FrequencyContainer<>("A", 4));
    assertTrue(comparator.compare("A", "B") < 0);
    assertTrue(comparator.compare("B", "A") > 0);
  }

  @Test
  void testCompareBothKeysMissing() {
    assertEquals(0, comparator.compare("A", "B"));
  }
}
