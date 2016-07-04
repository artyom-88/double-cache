package ru.ganev.doublecache;

import impl.SimpleCache;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ganev.doublecache.model.TestObject;

public class TestSimpleCache extends Assert {

    private final SimpleCache<String, TestObject> cache = new SimpleCache<>();

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
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
        assertEquals(5, cache.size());
        cache.remove("key1");
        assertEquals(4, cache.size());
    }

    @Test(expected = IllegalAccessException.class)
    public void testClear() throws IllegalAccessException {
        cache.clear();
        assertEquals(0, cache.size());
        cache.get("key1");
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
