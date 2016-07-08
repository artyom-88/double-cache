package ru.ganev.doublecache;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ganev.doublecache.impl.DoubleCache;
import ru.ganev.doublecache.impl.FileCache;
import ru.ganev.doublecache.model.TestObject;
import ru.ganev.doublecache.utils.TestUtility;

public class TestDoubleCache extends Assert {

    private final DoubleCache<String, TestObject> cache = new DoubleCache<>(4, 2);
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
    public void testClear() {
        testUtility.testClear();
    }

    @Test
    public void testGetFrequency() throws IOException, ClassNotFoundException {
        testUtility.testGetFrequency();
    }

    @Test
    public void testMostFrequentKeys() throws IOException, ClassNotFoundException {
        cache.put("key5", new TestObject("Object5", 5));
        for (int i = 0; i < 6; i++) {
            cache.get("key3");
        }
        for (int i = 0; i < 5; i++) {
            cache.get("key0");
        }
        for (int i = 0; i < 4; i++) {
            cache.get("key5");
        }
        for (int i = 0; i < 3; i++) {
            cache.get("key4");
        }
        for (int i = 0; i < 2; i++) {
            cache.get("key1");
        }
        cache.get("key2");
        Object[] keys = cache.mostFrequentKeys().toArray();
        assertEquals("key3", keys[0]);
        assertEquals("key0", keys[1]);
        assertEquals("key5", keys[2]);
        assertEquals("key4", keys[3]);
        assertEquals("key1", keys[4]);
        assertEquals("key2", keys[5]);
    }

    @After
    public void shutDown() {
        cache.clear();
        File file = new File(FileCache.DEFAULT_CACHE_PATH);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
