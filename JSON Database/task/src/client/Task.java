package client;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class Task {
    @Expose
    @Parameter(
            names = {"-t", "--type"},
            description = "Type of the request",
            order = 0)
    public String type;

    @Expose
    @Parameter(
            names = {"-k", "--key"},
            description = "Record key",
            order = 1)
    public String key;

    @Expose
    @Parameter(
            names = {"-v", "--value"},
            description = "Value to add",
            order = 2)
    public String value;

    @Parameter(
            names = {"-in", "--input-file"},
            description = "File containing the request as json string",
            order = 3)
    public String filename;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String readFromFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    public String toJson() {
        if (filename != null) {
            try {
                return readFromFile("~/IdeaProjects/JSON Database2/JSON Database/task/src/client/data/" + filename);
            } catch (IOException e) {
                System.out.println("Cannot read file: " + e.getMessage());
                System.exit(1);
            }
        }

        Map<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("key",key);
        map.put("value",value);
        return new Gson().toJson(map);
    }
}


