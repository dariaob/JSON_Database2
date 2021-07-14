package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import server.util.InputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


import server.util.OutputWriter;
import com.google.gson.Gson;


public class Main {
    final static String address = "127.0.0.1";
    final static int port = 23456;
    Map <String, String> request = new LinkedHashMap<>();

    private static Socket clientSocket;

    @Parameter(names = {"--type", "-t"})
    String type;
    @Parameter(names = {"--value", "-v"})
    String value;
    @Parameter(names = {"--key", "-k"})
    String key;
    @Parameter(names = {"--input", "-in"})
    String fileName;

    public static void main(final String[] args)  {

        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }
    String output = "";

    public void run() {

        createSocket();

        InputReader reader = new InputReader(clientSocket);
        OutputWriter outputWriter = new OutputWriter(clientSocket);

        System.out.println("Client started!");

        if (fileName == null) {
            createRequest(type, key, value);
            Gson gson = new Gson();
            output = gson.toJson(request);
        } else {
            try {
                File file = new File("src/client/data/" + fileName);
                Scanner scanner = new Scanner(file);
                output = scanner.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Sent: " + output);
        outputWriter.sendMsg(output);

        String received = reader.read().trim();
        System.out.println("Received: " + received);

        closeSocket();
    }

    private void createRequest(String type, String key, String value) {
        request.put("type", type);
        if (key != null){
            request.put("key", key);
        }
        if (value != null){
            request.put("value", value);
        }
    }

    /**
     * Close the connection
     */
    private static void closeSocket() {
        try {
            clientSocket.close();
        } catch (Exception ignored) {
        }
    }


    /**
     * Creating a socket to connect to the server
     */
    private static void createSocket(){
        while (true){
            try {
                clientSocket = new Socket();
                clientSocket.connect(new InetSocketAddress(address, port), 2000);
                return;
            } catch (Exception e) {
                System.out.println("\n" + e + "\n[CLIENT] Can't connect to the server");
            }
        }

    }

}