package ru.ganev.doublecache.model;

import java.util.UUID;

/**
 * Wrapper for storing object call frequency
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

    public T getObject() {
        frequency++;
        return object;
    }

    public String getUuid() {
        return uuid;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incFrequency() {
        frequency++;
    }
}
