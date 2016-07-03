package ru.ganev.doublecache;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ganev.doublecache.model.SimpleCache;
import ru.ganev.doublecache.model.TestObject;

public class TestSimpleCache extends Assert {

    private final SimpleCache<String, TestObject> cache = new SimpleCache<>();

    @Before
    public void setUp() {
        for (int i = 0; i < 3; i++) {
            cache.put("key" + i, TestObject.builder()
                    .field1("Object" + i)
                    .field2(i)
                    .build());
        }
    }

    @Test
    public void testGet() throws IllegalAccessException {
        assertEquals("Object1", cache.get("key1").getField1());
        assertEquals(1, cache.get("key1").getField2());
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

    @Test
    public void testMostFrequentKeys() throws IllegalAccessException {
        cache.get("key2");
        cache.get("key2");
        cache.get("key1");
        cache.mostFrequentKeys();
    }

    @Test
    public void testGetFrequency() throws IllegalAccessException {
        assertEquals(1, cache.getFrequency("key0"));
        cache.get("key1");
        assertEquals(2, cache.getFrequency("key1"));
    }

    @After
    public void shutDown() {
        cache.clear();
    }
}
