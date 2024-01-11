package com.ganev.doublecache.model;

import java.util.UUID;

/**
 * Wrapper for storing objects and their call frequency
 *
 * @param <T> object type
 */
public class FrequencyContainer<T> {

    private final T object;
    private final String uuid;
    private int frequency;

    public FrequencyContainer(T object) {
        this.object = object;
        this.uuid = UUID.randomUUID().toString();
        this.frequency = 1;
    }

    public FrequencyContainer(T object, int frequency) {
        this.object = object;
        this.uuid = UUID.randomUUID().toString();
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
    public String getUuid() {
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


    /**
     * Increments object call frequency
     */
    public void incFrequency() {
        frequency++;
    }
}
