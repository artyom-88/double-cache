package ru.ganev.doublecache;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ganev.doublecache.impl.MemoryCache;
import ru.ganev.doublecache.model.TestObject;
import ru.ganev.doublecache.utils.TestUtility;

public class TestSimpleCache extends Assert {

    private final MemoryCache<String, TestObject> cache = new MemoryCache<>();
    private final TestUtility testUtility = new TestUtility(cache);

    @Before
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
    public void testClear() throws IllegalAccessException {
        testUtility.testClear();
    }

    @Test
    public void testGetFrequency() throws IllegalAccessException, IOException, ClassNotFoundException {
        testUtility.testGetFrequency();
    }

    @Test
    public void testMostFrequentKeys() throws IllegalAccessException, IOException, ClassNotFoundException {
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

    @After
    public void shutDown() {
        cache.clear();
    }
}
