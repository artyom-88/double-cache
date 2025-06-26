package com.ganev.doublecache.impl;

import com.ganev.doublecache.model.AbstractCache;
import com.ganev.doublecache.model.FrequencyContainer;
import com.ganev.doublecache.validation.ValidatePutArgs;
import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Simple file cache
 *
 * @param <K> key type
 * @param <V> value type
 */
public class FileCache<K, V> extends AbstractCache<K, V> {

  public static final String DEFAULT_CACHE_PATH = Paths.get(".", "tmp").toString();
  public static final String FILE_EXT = ".temp";
  private String path = DEFAULT_CACHE_PATH;

  /** Creates new file cache with default path */
  public FileCache() {
    makeDir(DEFAULT_CACHE_PATH);
  }

  /**
   * Creates new file cache with custom {@code path}
   *
   * @param path custom path
   */
  public FileCache(String path) {
    File file = makeDir(path);
    if (file.isDirectory()) {
      this.path = path;
    } else {
      throw new IllegalArgumentException("Incorrect path: " + path);
    }
  }

  @Override
  @ValidatePutArgs
  public void put(K key, V value) {
    FrequencyContainer<V> container = new FrequencyContainer<>(value);
    String filePath = createFilePath(container.getUuid());
    frequencyMap.put(key, container);
    this.write(filePath, value);
  }

  @Override
  public V get(K key) {
    if (contains(key)) {
      FrequencyContainer<V> container = frequencyMap.get(key);
      String filePath = createFilePath(container.getUuid());
      V object = read(filePath);
      container.incFrequency();
      return object;
    }
    return null;
  }

  @Override
  public V remove(K key) {
    FrequencyContainer<V> container = frequencyMap.get(key);
    if (container == null) {
      return null;
    }
    File file = new File(createFilePath(container.getUuid()));
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

  private File makeDir(String path) {
    File tmpDir = new File(path);
    if (!tmpDir.exists()) {
      tmpDir.mkdirs();
    }
    return tmpDir;
  }

  private String createFilePath(UUID uuid) {
    return Paths.get(path, uuid + FILE_EXT).toString();
  }

  private void write(String filePath, V value) {
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

  private V read(String filePath) {
    try (FileInputStream fileStream = new FileInputStream(filePath);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream)) {
      @SuppressWarnings("unchecked cast")
      V object = (V) objectStream.readObject();
      return object;
    } catch (FileNotFoundException e) {
      throw new RuntimeException(String.format("File %s doesn't exist", filePath));
    } catch (IOException e) {
      throw new RuntimeException("I/O Error");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class of a serialized object cannot be found");
    }
  }
}
