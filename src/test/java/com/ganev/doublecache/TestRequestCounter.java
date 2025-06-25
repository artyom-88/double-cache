package com.ganev.doublecache;

import static org.junit.jupiter.api.Assertions.*;

import com.ganev.doublecache.model.RequestCounter;
import org.junit.jupiter.api.Test;

public class TestRequestCounter {

  @Test
  public void testIncrementAndReset() {
    RequestCounter requestCounter = new RequestCounter(5);
    requestCounter.increment();
    assertEquals(1, requestCounter.getRequestsAmount());

    requestCounter.reset();
    assertEquals(0, requestCounter.getRequestsAmount());
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
}
