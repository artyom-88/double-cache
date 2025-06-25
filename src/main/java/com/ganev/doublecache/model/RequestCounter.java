package com.ganev.doublecache.model;

import java.util.concurrent.atomic.AtomicInteger;

/** Represents a counter for tracking requests. */
public class RequestCounter {
  private final AtomicInteger requestCount = new AtomicInteger(0);
  private final int maxRequests;

  public RequestCounter(int maxRequests) {
    if (maxRequests < 1) {
      throw new IllegalArgumentException("max requests count must be positive");
    }
    this.maxRequests = maxRequests;
  }

  public int getRequestCount() {
    return requestCount.get();
  }

  /** Increments the request count. */
  public void increment() {
    requestCount.incrementAndGet();
  }

  /** Resets the request count to zero. */
  public void reset() {
    requestCount.set(0);
  }

  /**
   * Checks if the maximum requests amount has been reached.
   *
   * @return true if the maximum requests amount has been reached, false otherwise.
   */
  public boolean hasReachedMaxRequests() {
    return requestCount.get() >= maxRequests;
  }

  @Override
  public String toString() {
    return "RequestCounter{requestCount="
        + requestCount.get()
        + ", maxRequests="
        + maxRequests
        + '}';
  }
}
