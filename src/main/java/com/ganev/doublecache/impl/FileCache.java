package com.ganev.doublecache.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;

import com.ganev.doublecache.model.AbstractCache;
import com.ganev.doublecache.model.FrequencyContainer;

/**
 * Simple file cache
 *
 * @param <K> key type
 * @param <V> value type
 */
public class FileCache<K, V> extends AbstractCache<K, V> {

    public static final String DEFAULT_CACHE_PATH = String.join(FileSystems.getDefault().getSeparator(), ".", "tmp", "");
    private String path;

    /**
     * Creates new file cache with default path
     */
    public FileCache() {
        makeDir();
    }

    /**
     * Creates new file cache with custom {@code path}
     *
     * @param path custom path
     */
    public FileCache(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            this.path = path;
        } else {
            throw new IllegalArgumentException("Incorrect path: " + path);
        }
        makeDir();
    }

    @Override
    public void put(K key, V value) {
        FrequencyContainer<V> container = new FrequencyContainer<>(value);
        String filePath = path + container.getUuid() + ".temp";
        frequencyMap.put(key, container);
        try (FileOutputStream fileStream = new FileOutputStream(filePath);
             ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
            objectStream.writeObject(value);
            objectStream.flush();
            fileStream.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("File %s doesn't exist", filePath));
        } catch (IOException e) {
            throw new RuntimeException("I/O Error");
        }
    }

    @Override
    public V get(K key) {
        if (this.contains(key)) {
            FrequencyContainer<V> container = frequencyMap.get(key);
            String filePath = createFilePath(container.getUuid());
            try (FileInputStream fileStream = new FileInputStream(filePath);
                 ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
                @SuppressWarnings("unchecked cast")
                V object = (V) objectStream.readObject();
                container.incFrequency();
                return object;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(String.format("File %s doesn't exist", filePath));
            } catch (IOException e) {
                throw new RuntimeException("I/O Error");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class of a serialized object cannot be found");
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        File file = new File(createFilePath(frequencyMap.get(key).getUuid()));
        return file.delete() ? super.remove(key) : null;
    }

    @Override
    public void clear() {
        frequencyMap.values().stream()
                .map(FrequencyContainer::getUuid)
                .map(this::createFilePath)
                .map(File::new)
                .forEach(File::delete);
        super.clear();
    }

    private void makeDir() {
        if (path == null) {
            path = DEFAULT_CACHE_PATH;
        }
        File tmpDir = new File(path);
        if (!tmpDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            tmpDir.mkdirs();
        }
    }

    private String createFilePath(String uuid) {
        return path + uuid + ".temp";
    }
}
