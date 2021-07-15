package server.requests;

import com.beust.jcommander.Parameter;
import com.google.gson.JsonElement;

public class Request {
        @Parameter(names = {"-type", "--t"}, description = "The type of the request")
        private String type;

        @Parameter(names = {"-key", "--k"}, description = "The Record key")
        private JsonElement key;

        @Parameter(names = {"-value", "--v"}, description = "The text value to add")
        private JsonElement value;

        //file for reading
        @Parameter(names = {"-in", "--commandfromfile"}, description = "The text with commands from file")
        private String commandfromfile;


        public Request() {
        }

        public String getType() {
            return type;
        }

        public JsonElement getKey() {
            return key;
        }

        public JsonElement getValue() {
            return value;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setKey(JsonElement key) {
            this.key = key;
        }

        public void setValue(JsonElement value) {
            this.value = value;
        }
    }
