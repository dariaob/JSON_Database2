package server;
import com.google.gson.Gson;
import server.Database;
import server.util.OutputWriter;
import server.util.InputReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;


public class Worker implements Runnable{
    private final InputReader inputReader;
    private final OutputWriter outputWriter;
    private final Socket socket;
    private final ServerSocket serverSocket;
    private final Database database;
    private static final boolean DEBUG_MODE = false;

    Map <String, String> text = new LinkedHashMap<>();

    public Worker (final Socket socket, ServerSocket serverSocket, Database database) {
        if (DEBUG_MODE) {
            System.out.println("Client connected!");
        }
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.database = database;
        this.inputReader = new InputReader(socket);
        this.outputWriter = new OutputWriter(socket);
    }



    @Override
    public void run() {
        final String rawMsg = inputReader.read().trim();
        GsonFromJson gsonFromJson = new GsonFromJson(rawMsg);
        gsonFromJson.getString();

        String command = gsonFromJson.getType();
        String key = gsonFromJson.getKey();
        String value = gsonFromJson.getValue();


        switch (command) {
            case "get":
                String result = database.get(key);
                if (result == null) {
                    text.put("response", "ERROR");
                    text.put("reason", "No such key");
                } else {
                    text.put("response", "OK");
                    text.put("value", result.trim());
                }
                sentAnswer(text);
                break;
            case "set":
                if (database.set(key, value)) {
                    text.put("response", "OK");
                } else {
                    text.put("response", "ERROR");
                }
                sentAnswer(text);
                break;
            case "delete":
                if (database.delete(key)) {
                    text.put("response", "OK");
                } else {
                    text.put("response", "ERROR");
                    text.put("reason", "No such key");
                }
                sentAnswer(text);
                break;
            case "exit":
                text.put("response", "OK");
                sentAnswer(text);
                closeSocket();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void sentAnswer(Map <String, String> stringMap) {
        Gson gson = new Gson();
        String answer = gson.toJson(stringMap);
        outputWriter.sendMsg(answer.trim());
    }



    private void closeSocket() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }
    }

