package com.ganev.doublecache.model;

import java.util.concurrent.atomic.AtomicInteger;

/** Represents a counter for tracking requests. */
public class RequestCounter {
  private final AtomicInteger requestsAmount = new AtomicInteger(0);
  private final int maxRequests;

  public RequestCounter(int maxRequests) {
    if (maxRequests < 1) {
      throw new IllegalArgumentException("max requests count must be positive");
    }
    this.maxRequests = maxRequests;
  }

  public int getRequestsAmount() {
    return requestsAmount.get();
  }

  /** Increments the request count. */
  public void increment() {
    requestsAmount.incrementAndGet();
  }

  /** Resets the request count to zero. */
  public void reset() {
    requestsAmount.set(0);
  }

  /**
   * Checks if the maximum requests amount has been reached.
   *
   * @return true if the maximum requests amount has been reached, false otherwise.
   */
  public boolean hasReachedMaxRequests() {
    return requestsAmount.get() >= maxRequests;
  }
}
