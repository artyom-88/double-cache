package ru.ganev.doublecache;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSimpleCache extends Assert {

    private final SimpleCache<String, String> cache = new SimpleCache<>();

    @Before
    public void setUp() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
    }

    @Test(expected = IllegalAccessException.class)
    public void testRemove() throws IllegalAccessException {
        cache.remove("key1");
        cache.get("key1");
    }

    @Test
    public void testSize() {
        assertEquals(3, cache.size());
        cache.remove("key1");
        assertEquals(2, cache.size());
    }

    @Test
    public void testClear() {
        cache.clear();
        assertEquals(0, cache.size());
    }

    @After
    public void shutDown() {
        cache.clear();
    }
}
