package server.util;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class InputReader {
    private final Socket socket;
    private DataInputStream dataInputStream;

    public InputReader(final Socket socket) {
        this.socket = socket;
        createInputStream();
    }

    public String read() {
        try {
            return dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void createInputStream() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (Exception ignored) {

        }
    }

}