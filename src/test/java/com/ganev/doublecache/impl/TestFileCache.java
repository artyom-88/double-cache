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

  @AfterEach
  public void shutDown() {
    cache.clear();
    File file = new File(FileCache.DEFAULT_CACHE_PATH);
    testUtility.deleteRecursively(file);
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

  @Test
  public void testPutNullKey() {
    testUtility.testPutNullKey();
  }

  @Test
  public void testPutNullValue() {
    testUtility.testPutNullValue();
  }

  @Test
  public void testRemoveNonExistentKey() {
    testUtility.testRemoveNonExistentKey();
  }

  @Test
  public void testGetNonExistentKey() throws IOException, ClassNotFoundException {
    testUtility.testGetNonExistentKey();
  }

  @Test
  public void testRemoveTwice() {
    testUtility.testRemoveTwice();
  }

  @Test
  public void testClearEmptyCache() {
    testUtility.testClearEmptyCache();
  }

  @Test
  public void testPutAllEmptyMap() {
    testUtility.testPutAllEmptyMap();
  }

  @Test
  public void testCustomPathConstructorCreatesDirectory() {
    String customPath = "./tmp/custom-cache-dir";
    File dir = new File(customPath);
    if (dir.exists()) dir.delete();
    FileCache<String, TestObject> customCache = new FileCache<>(customPath);
    assertTrue(dir.exists() && dir.isDirectory());
    customCache.clear();
    dir.delete();
  }

  @Test
  public void testCustomPathConstructorThrowsOnInvalidPath() {
    String filePath = "./tmp/not-a-dir.txt";
    try {
      new File(filePath).createNewFile();
      assertThrows(IllegalArgumentException.class, () -> new FileCache<>(filePath));
    } catch (IOException e) {
      fail("Setup failed: " + e.getMessage());
    } finally {
      new File(filePath).delete();
    }
  }

  @Test
  public void testPutNonSerializableValueThrows() {
    class NonSerializable {}
    FileCache<String, Object> cache = new FileCache<>();
    assertThrows(RuntimeException.class, () -> cache.put("bad", new NonSerializable()));
  }
}
