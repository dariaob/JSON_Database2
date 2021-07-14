package server;

public class Arguments {
    String type;
    String value;
    String key;
    String filename;


    Arguments (String type, String key, String value, String filename){
        this.type = type;
        this.key = key;
        this.value = value;
        this.filename = filename;
    }


    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String getFilename() {
        return filename;
    }
}
