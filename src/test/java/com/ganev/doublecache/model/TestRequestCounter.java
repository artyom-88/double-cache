package com.ganev.doublecache.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestRequestCounter {

  @Test
  public void testIncrementAndReset() {
    RequestCounter requestCounter = new RequestCounter(5);
    requestCounter.increment();
    assertEquals(1, requestCounter.getRequestCount());

    requestCounter.reset();
    assertEquals(0, requestCounter.getRequestCount());
  }

  @Test
  public void testHasReachedMaxRequestsAfterExceeding() {
    RequestCounter requestCounter = new RequestCounter(2);
    requestCounter.increment();
    requestCounter.increment();
    requestCounter.increment(); // Exceeds max
    assertTrue(requestCounter.hasReachedMaxRequests());
  }

  @Test
  public void testReset() {
    RequestCounter requestCounter = new RequestCounter(3);
    requestCounter.increment();
    requestCounter.increment();
    requestCounter.increment();
    assertTrue(requestCounter.hasReachedMaxRequests());

    requestCounter.reset();
    assertFalse(requestCounter.hasReachedMaxRequests());
  }

  @Test
  public void testNegativeMaxRequestsAmount() {
    assertThrows(IllegalArgumentException.class, () -> new RequestCounter(-1));
  }

  @Test
  public void testZeroMaxRequestsAmount() {
    assertThrows(IllegalArgumentException.class, () -> new RequestCounter(0));
  }

  @Test
  public void testToString() {
    RequestCounter requestCounter = new RequestCounter(3);
    requestCounter.increment();
    String str = requestCounter.toString();
    assertTrue(str.contains("requestCount=1"));
    assertTrue(str.contains("maxRequests=3"));
  }
}
