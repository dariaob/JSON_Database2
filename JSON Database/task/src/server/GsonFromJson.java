package server;

import com.google.gson.Gson;

public class GsonFromJson {
    String jsonString;
    Arguments arguments;

    GsonFromJson(String jsonString){
        this.jsonString = jsonString;

    }

    public boolean getString(){
        Gson gson = new Gson();
        arguments = gson.fromJson(jsonString, Arguments.class);
        return arguments != null;
    }

    public String getType(){
        return arguments.getType();
    }

    public String getKey(){
        return arguments.getKey();
    }

    public String getValue(){
        return arguments.getValue();
    }

}
