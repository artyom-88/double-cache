package com.ganev.doublecache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ganev.doublecache.model.RequestCounter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

public class TestConcurrentRequestCounter {

  private static final int THREAD_COUNT = 10;
  private static final int MAX_REQUESTS = 100;

  @RepeatedTest(THREAD_COUNT)
  @DisplayName("Concurrent Increment and Reset")
  public void testConcurrentIncrementAndReset() throws InterruptedException {
    RequestCounter requestCounter = new RequestCounter(MAX_REQUESTS);
    CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    for (int i = 0; i < THREAD_COUNT; i++) {
      executorService.submit(
          () -> {
            requestCounter.increment();
            latch.countDown();
          });
    }

    latch.await();
    executorService.shutdown();

    assertEquals(THREAD_COUNT, requestCounter.getRequestCount());
    requestCounter.reset();
    assertEquals(0, requestCounter.getRequestCount());
  }
}
