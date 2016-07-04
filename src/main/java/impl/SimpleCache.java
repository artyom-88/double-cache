package impl;

import ru.ganev.doublecache.model.AbstractCache;
import ru.ganev.doublecache.model.FrequencyContainer;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple cache for storing objects in RAM
 *
 * @param <K> key
 * @param <V> object type
 */
public class SimpleCache<K, V> extends AbstractCache<K, V> {

    @Override
    public void put(K key, V value) {
        hash.put(key, new FrequencyContainer<>(value));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        hash.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new FrequencyContainer<>(e.getValue())));
    }

    @Override
    public V get(K key) throws IllegalAccessException {
        if (hash.containsKey(key)) {
            return hash.get(key).getObject();
        } else {
            throw new IllegalAccessException(String.format("Key %s doesn't exist", key));
        }
    }

    @Override
    public void clear() {
        hash.clear();
    }

    @Override
    public void remove(K key) {
        hash.remove(key);
    }
}
