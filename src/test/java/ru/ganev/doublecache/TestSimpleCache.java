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
    public void testGet() throws IllegalAccessException, IOException, ClassNotFoundException {
        testUtility.testGet();
    }

    @Test(expected = IllegalAccessException.class)
    public void testRemove() throws IllegalAccessException, IOException, ClassNotFoundException {
        testUtility.testRemove();
    }

    @Test
    public void testSize() {
        testUtility.testSize();
    }

    @Test
    public void testClear() throws IllegalAccessException {
        testUtility.testClear();
    }

//    @Test
//    public void testMostFrequentKeys() throws IllegalAccessException {
//        cache.get("key1");
//        cache.get("key1");
//        cache.get("key2");
//        cache.get("key2");
//        cache.get("key2");
//        cache.get("key4");
//        Set<?> set = cache.mostFrequentKeys();
//        System.out.println(set);
//    }

    @Test
    public void testGetFrequency() throws IllegalAccessException, IOException, ClassNotFoundException {
        testUtility.testGetFrequency();
    }

    @After
    public void shutDown() {
        cache.clear();
    }
}
