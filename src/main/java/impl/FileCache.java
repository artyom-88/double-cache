package impl;

import ru.ganev.doublecache.model.AbstractCache;
import ru.ganev.doublecache.model.FrequencyContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class FileCache<K, V> extends AbstractCache<K, V> {

    public static final String DEFAULT_CACHE_PATH = "./tmp/";
    private String path;

    public FileCache() {
        makeDir();
    }

    public void setPath(String path) throws IllegalArgumentException {
        File file = new File(path);
        if (file.isDirectory()) {
            this.path = path;
        } else {
            throw new IllegalArgumentException("Incorrect path");
        }
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
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public V get(K key) throws IllegalAccessException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void remove(K key) {

    }

    private void makeDir() {
        if (path == null) {
            path = DEFAULT_CACHE_PATH;
        }
        File tmpDir = new File(path);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
    }
}
