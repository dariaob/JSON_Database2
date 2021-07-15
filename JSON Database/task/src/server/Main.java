package server;
import com.google.gson.JsonArray;
import server.Worker;
import server.util.InputReader;
import server.util.OutputWriter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static ServerSocket serverSocket;
    private static final String filename = "./src/server/data/db.json";
    public static void main(String[] args) {

        sayHello();
        createSocket();
        Database.INSTANCE.init();
        startNewThread();
        closeSocket();
    }

    public static void sayHello() {
        System.out.println("Server started!");
    }

    public static void createSocket() {
        final String address = "127.0.0.1";
        final int port = 23456;
        while (true) {
            try {
                serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
                return;
            } catch (Exception ignored) {
                System.out.println("Oops. No connection");;
            }
        }
    }
    public static Socket getConnection(){
        try {
            return serverSocket.accept();
        } catch (Exception ignored) {

        }
        return null;
    }


    private static void closeSocket() {
        try {
            serverSocket.close();
        } catch (Exception ignored) {
        }
    }

    public static void startNewThread() {
        while (!serverSocket.isClosed()){
            Socket socket = null;
            try {
                socket = getConnection();
                InputReader inputReader = new InputReader(socket);
                OutputWriter outputWriter = new OutputWriter(socket);
                Thread thread = new Worker(socket, inputReader, outputWriter, serverSocket);
                thread.start();
            } catch (Exception e) {
                try{
                serverSocket.close();
            } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}