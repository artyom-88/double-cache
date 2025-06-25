package com.ganev.doublecache.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Wrapper for storing objects and their call frequency
 *
 * @param <T> object type
 */
public class FrequencyContainer<T> {

  private final T object;
  private final UUID uuid;
  private int frequency;

  public FrequencyContainer(T object) {
    this.object = object;
    this.uuid = UUID.randomUUID();
    this.frequency = 1;
  }

  public FrequencyContainer(T object, int frequency) {
    this.object = object;
    this.uuid = UUID.randomUUID();
    this.frequency = frequency;
  }

  /**
   * Getter for containing object
   *
   * @return object
   */
  public T getObject() {
    return object;
  }

  /**
   * Getter for object uuid
   *
   * @return uuid
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Returns containing object call frequency
   *
   * @return frequency
   */
  public int getFrequency() {
    return frequency;
  }

  /** Increments object call frequency */
  public void incFrequency() {
    frequency++;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FrequencyContainer<?> that)) return false;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public String toString() {
    return "FrequencyContainer{"
        + "object="
        + object
        + ", uuid='"
        + uuid
        + '\''
        + ", frequency="
        + frequency
        + '}';
  }
}
