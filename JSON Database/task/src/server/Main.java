package server;
import server.Worker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static ServerSocket serverSocket;
    private static final String filename = "./src/server/data/db.json";
    public static void main(String[] args) {
        Database database = new Database(filename);
        sayHello();
        createSocket();
        createClientSocket(database);
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

    public static void createClientSocket(Database database) {
        while (!serverSocket.isClosed()) {
            final Socket clientSocket = getConnection();
            if (clientSocket != null){
                new Thread(new Worker(clientSocket, serverSocket, database)).start();
            }
        }
    }
    private static void closeSocket() {
        try {
            serverSocket.close();
        } catch (Exception ignored) {
        }
    }
}