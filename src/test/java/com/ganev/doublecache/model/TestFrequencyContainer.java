package com.ganev.doublecache.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestFrequencyContainer {

  @Test
  void testConstructorAndGetters() {
    String obj = "test";
    FrequencyContainer<String> fc = new FrequencyContainer<>(obj);
    assertEquals(obj, fc.getObject());
    assertNotNull(fc.getUuid());
    assertEquals(1, fc.getFrequency());
  }

  @Test
  void testConstructorWithFrequency() {
    String obj = "test";
    int freq = 5;
    FrequencyContainer<String> fc = new FrequencyContainer<>(obj, freq);
    assertEquals(obj, fc.getObject());
    assertNotNull(fc.getUuid());
    assertEquals(freq, fc.getFrequency());
  }

  @Test
  void testIncFrequency() {
    FrequencyContainer<String> fc = new FrequencyContainer<>("test");
    fc.incFrequency();
    assertEquals(2, fc.getFrequency());
  }

  @Test
  void testEqualsAndHashCode() {
    FrequencyContainer<String> fc1 = new FrequencyContainer<>("a");
    FrequencyContainer<String> fc2 = new FrequencyContainer<>("a");
    assertNotEquals(fc1, fc2);
    assertNotEquals(fc1.hashCode(), fc2.hashCode());
    assertEquals(fc1, fc1);
  }

  @Test
  void testToString() {
    FrequencyContainer<String> fc = new FrequencyContainer<>("test");
    String str = fc.toString();
    assertTrue(str.contains("FrequencyContainer"));
    assertTrue(str.contains("test"));
  }
}
