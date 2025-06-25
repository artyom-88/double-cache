package com.ganev.doublecache.impl;

import com.ganev.doublecache.model.AbstractCache;
import com.ganev.doublecache.model.FrequencyContainer;
import java.io.*;
import java.nio.file.FileSystems;

/**
 * Simple file cache
 *
 * @param <K> key type
 * @param <V> value type
 */
public class FileCache<K, V> extends AbstractCache<K, V> {

  public static final String DEFAULT_CACHE_PATH =
      String.join(FileSystems.getDefault().getSeparator(), ".", "tmp", "");
  private String path;

  /** Creates new file cache with default path */
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
    String filePath = this.createFilePath(container.getUuid());
    frequencyMap.put(key, container);
    this.write(filePath, value);
  }

  @Override
  public V get(K key) {
    if (this.contains(key)) {
      FrequencyContainer<V> container = frequencyMap.get(key);
      String filePath = this.createFilePath(container.getUuid());
      V object = this.read(filePath);
      container.incFrequency();
      return object;
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
