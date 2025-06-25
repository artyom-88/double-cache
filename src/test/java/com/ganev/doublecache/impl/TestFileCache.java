package com.ganev.doublecache.impl;

import com.ganev.doublecache.model.TestObject;
import com.ganev.doublecache.utils.TestUtility;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFileCache extends Assertions {

  private final FileCache<String, TestObject> cache = new FileCache<>();
  private final TestUtility testUtility = new TestUtility(cache);

  @BeforeEach
  public void setUp() {
    testUtility.setUp();
  }

  @Test
  public void testPut() throws IOException, ClassNotFoundException {
    testUtility.testPut();
  }

  @Test
  public void testPutAll() {
    testUtility.testPutAll();
  }

  @Test
  public void testGet() throws IOException, ClassNotFoundException {
    testUtility.testGet();
  }

  @Test
  public void testRemove() throws IOException, ClassNotFoundException {
    testUtility.testRemove();
  }

  @Test
  public void testSize() {
    testUtility.testSize();
  }

  @Test
  public void testContains() {
    testUtility.testContains();
  }

  @Test
  public void testClear() {
    testUtility.testClear();
  }

  @Test
  public void testMostFrequentKeys() throws IOException, ClassNotFoundException {
    for (int i = 4; i > 0; i--) {
      for (int j = 0; j < i; j++) {
        cache.get("key" + i);
      }
    }
    Object[] keys = cache.mostFrequentKeys().toArray();
    for (int i = 0, j = 4; i < keys.length; i++, j--) {
      assertEquals("key" + j, keys[i]);
    }
  }

  @Test
  public void testGetFrequency() throws IOException, ClassNotFoundException {
    testUtility.testGetFrequency();
  }

  @AfterEach
  public void shutDown() {
    cache.clear();
    File file = new File(FileCache.DEFAULT_CACHE_PATH);
    file.delete();
  }

  @Test
  public void testPutNullKey() {
    testUtility.testPutNullKey();
  }

  @Test
  public void testPutNullValue() {
    testUtility.testPutNullValue();
  }
}
