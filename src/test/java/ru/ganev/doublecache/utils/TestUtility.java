package ru.ganev.doublecache.utils;

import java.io.IOException;

import ru.ganev.doublecache.model.Cache;
import ru.ganev.doublecache.model.TestObject;

import static org.junit.Assert.assertEquals;

public class TestUtility {

    private final Cache<String, TestObject> cache;

    public TestUtility(Cache<String, TestObject> cache) {
        this.cache = cache;
    }

    public void setUp() {
        for (int i = 0; i < 5; i++) {
            cache.put("key" + i, TestObject.builder()
                    .field1("Object" + i)
                    .field2(i)
                    .build());
        }
    }

    public void testGet() throws IllegalAccessException, IOException, ClassNotFoundException {
        assertEquals("Object1", cache.get("key1").getField1());
        assertEquals(1, cache.get("key1").getField2());
    }

    public void testRemove() throws IllegalAccessException, IOException, ClassNotFoundException {
        cache.remove("key1");
        cache.get("key1");
    }

    public void testSize() {
        assertEquals(5, cache.size());
        cache.remove("key1");
        assertEquals(4, cache.size());
    }

    public void testClear() throws IllegalAccessException {
        cache.clear();
        assertEquals(0, cache.size());
    }

    public void testGetFrequency() throws IllegalAccessException, IOException, ClassNotFoundException {
        assertEquals(1, cache.getFrequency("key0"));
        cache.get("key1");
        assertEquals(2, cache.getFrequency("key1"));
    }
}
