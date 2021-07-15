package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public enum Database {
    INSTANCE;

    private JsonObject database;
    private ReadWriteLock lock;
    private final Lock readLock;
    private final Lock writeLock;

    {
        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }
    Database() {}

    public void init() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("./src/server/data/db.json")));
            database = new Gson().fromJson(content, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Setting data to Json
    public void set(JsonElement key, JsonElement value) {
        try {
            writeLock.lock();
            //adding pait key value if db is empty
            if (database == null){
                database = new JsonObject();
                database.add(key.getAsString(), value);
            } else {
                //if database is not Empty checkout if key is primitive or array
                if (key.isJsonPrimitive()){
                    database.add(key.getAsString(), value);
                }
                else if (key.isJsonArray()){
                    JsonArray keys = key.getAsJsonArray();
                    String addition = keys.remove(keys.size() - 1).getAsString();
                    findElement(keys, true).getAsJsonObject().add(addition, value);
                } else {
                    throw new NoSuchElementException();
                }
            }
            writoToDataBase();
        } finally {
            writeLock.unlock();
        }
    }

    public JsonElement get(JsonElement key) {
        try {
            readLock.lock();
            if (key.isJsonPrimitive() && database.has(key.getAsString())){
                return (JsonElement)database.get(key.getAsString());
            }
            else if (key.isJsonArray()){
                return (JsonElement) findElement(key.getAsJsonArray(), true);
            }
            throw new NoSuchElementException();
        } finally {
            readLock.unlock();
        }
    }

    public void delete(JsonElement key){
        try {
            writeLock.lock();
            if (key.isJsonPrimitive() && database.has(key.getAsString())){
                database.remove(key.getAsString());
            }
            else if (key.isJsonArray()){
                JsonArray keys = key.getAsJsonArray();
                String toDelete = keys.remove(keys.size() - 1).getAsString();
                findElement(keys, true).getAsJsonObject().remove(toDelete);
            }
            writoToDataBase();
        } finally {
            writeLock.unlock();
        }
    }



    private JsonElement findElement (JsonArray keys, boolean ifAbsent){
        JsonElement tmp = database;
        if (ifAbsent) {
            //searching keys
            for(JsonElement key: keys){
                //if there is no such key create a new instance
                if(!tmp.getAsJsonObject().has(key.getAsString())){
                    tmp.getAsJsonObject().add(key.getAsString(), new JsonObject());
                }
                //if the key is found use it
                tmp = tmp.getAsJsonObject().get(key.getAsString());
            }
        }
        else {
            for(JsonElement key: keys){
                //if the key isn't primitive or it doesn't exist in databse
                if(!key.isJsonPrimitive() || !tmp.getAsJsonObject().has(key.getAsString())) {
                    throw new NoSuchElementException();
                }
                tmp = tmp.getAsJsonObject().get(key.getAsString());
            }
        }
        return tmp;
    }



    private void writoToDataBase() {
        try {
            FileWriter writer = new FileWriter("./src/client/data/db.json");
            writer.write(database.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

