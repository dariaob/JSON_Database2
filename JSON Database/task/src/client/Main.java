package client;

import com.beust.jcommander.JCommander;
import com.google.gson.GsonBuilder;
import server.util.InputReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import server.util.OutputWriter;
import com.google.gson.Gson;


public class Main {
    final static String address = "127.0.0.1";
    final static int port = 23456;

    private static Socket clientSocket;

    public static void main(final String[] args) {
        Task task = new Task();
        JCommander jCommander = new JCommander(task);
        jCommander.setProgramName("JSON Database");

        JCommander.newBuilder()
                .addObject(task)
                .build()
                .parse(args);

        createSocket();

        InputReader reader = new InputReader(clientSocket);
        OutputWriter outputWriter = new OutputWriter(clientSocket);

        System.out.println("Client started!");

        String request = task.toJson();
        System.out.println("Sent: " + request);
        outputWriter.sendMsg(request);

        String received = reader.read();
        System.out.println("Received: " + received);

        closeSocket();
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