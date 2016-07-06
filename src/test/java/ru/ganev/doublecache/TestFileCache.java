package ru.ganev.doublecache;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ganev.doublecache.impl.FileCache;
import ru.ganev.doublecache.model.TestObject;
import ru.ganev.doublecache.utils.TestUtility;

public class TestFileCache extends Assert {

    private final FileCache<String, TestObject> cache = new FileCache<>();
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
    public void testMostFrequentKeys() throws IOException, ClassNotFoundException {
        testUtility.testMostFrequentKeys();
    }

    @Test
    public void testGetFrequency() throws IOException, ClassNotFoundException {
        testUtility.testGetFrequency();
    }

    @After
    public void shutDown() {
        cache.clear();
        File file = new File(FileCache.DEFAULT_CACHE_PATH);
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
