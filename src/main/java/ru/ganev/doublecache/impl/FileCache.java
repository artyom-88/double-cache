package ru.ganev.doublecache.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ru.ganev.doublecache.model.AbstractCache;
import ru.ganev.doublecache.model.FrequencyContainer;

public class FileCache<K, V> extends AbstractCache<K, V> {

    public static final String DEFAULT_CACHE_PATH = "./tmp/";
    private String path;

    public FileCache() {
        makeDir();
    }

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
        hash.put(key, container);
        try {
            FileOutputStream fileStream = new FileOutputStream(filePath);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
            objectStream.writeObject(value);
            objectStream.flush();
            objectStream.close();
            fileStream.flush();
            fileStream.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("File %s doesn't exist", filePath));
        }
    }

    @Override
    public V get(K key) {
        if (this.contains(key)) {
            FrequencyContainer<V> container = hash.get(key);
            String filePath = createFilePath(container.getUuid());
            try {
                FileInputStream fileStream = new FileInputStream(filePath);
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                @SuppressWarnings("unchecked cast")
                V object = (V) objectStream.readObject();
                fileStream.close();
                objectStream.close();
                container.incFrequency();
                return object;
            } catch (IOException e) {
                throw new RuntimeException("Error while reading stream header");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class of a serialized object cannot be found");
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        File file = new File(createFilePath(hash.get(key).getUuid()));
        if (file.delete()) {
            return super.remove(key);
        }
        return null;
    }

    @Override
    public void clear() {
        hash.values().stream()
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
