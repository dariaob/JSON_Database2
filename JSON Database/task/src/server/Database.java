package server;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {

    ReadWriteLock lock;
    Lock readLock;
    Lock writeLock;
    String filename;

    public Database(String filename) {
        this.filename = filename;
        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        try {
            writeLock.lock();

            FileWriter writer = new FileWriter(filename);
            writer.write("{}");
            writer.close();

            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean set(String key, String value) {
        boolean result = false;
        try {
            writeLock.lock();

            FileReader reader = new FileReader(filename);
            Map<String, String> map = new Gson().fromJson(reader, Map.class);
            reader.close();

            map.put(key, value);
            FileWriter writer = new FileWriter(filename);
            writer.write(new Gson().toJson(map));
            writer.close();

            writeLock.unlock();

            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String get(String key) {
        String result = null;
        try {
            readLock.lock();

            FileReader reader = new FileReader(filename);
            Map <String, String> map = new Gson().fromJson(reader, Map.class);

            reader.close();

            result = map.get(key);

            readLock.unlock();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean delete(String key){
        boolean result = false;

        try {
            writeLock.lock();

            FileReader reader = new FileReader(filename);
            Map<String, String> map = new Gson().fromJson(reader, Map.class);
            reader.close();

            if (map.containsKey(key)) {
                map.remove(key);
                result = true;
            }

            FileWriter writer = new FileWriter(filename);
            writer.write(new Gson().toJson(map));
            writer.close();

            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}