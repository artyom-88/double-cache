package com.ganev.doublecache.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ganev.doublecache.model.Cache;
import com.ganev.doublecache.model.TestObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    public void testPut() throws IOException, ClassNotFoundException {
        cache.put("test0", new TestObject("test0", 0));
        assertTrue(cache.contains("test0"));
        assertEquals(0, cache.get("test0").getField2());
    }

    public void testPutAll() {
        cache.clear();
        Map<String, TestObject> map = new HashMap<>();
        map.put("test0", new TestObject("test0", 0));
        map.put("test1", new TestObject("test1", 1));
        map.put("test2", new TestObject("test2", 2));
        cache.putAll(map);
        assertTrue(cache.contains("test0"));
        assertTrue(cache.contains("test1"));
        assertTrue(cache.contains("test2"));
        assertEquals(3, cache.size());
    }

    public void testGet() throws IOException, ClassNotFoundException {
        assertEquals("Object1", cache.get("key1").getField1());
        assertEquals(1, cache.get("key1").getField2());
    }

    public void testRemove() throws IOException, ClassNotFoundException {
        cache.remove("key1");
        assertNull(cache.get("key1"));
    }

    public void testSize() {
        assertEquals(5, cache.size());
        cache.remove("key1");
        assertEquals(4, cache.size());
    }

    public void testClear() {
        cache.clear();
        assertEquals(0, cache.size());
    }

    public void testContains() {
        assertTrue(cache.contains("key0"));
        assertFalse(cache.contains("zzz"));
    }

    public void testGetFrequency() throws IOException, ClassNotFoundException {
        assertEquals(1, cache.getFrequency("key0"));
        cache.get("key1");
        assertEquals(2, cache.getFrequency("key1"));
    }

}
