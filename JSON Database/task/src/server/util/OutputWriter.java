package server.util;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class OutputWriter {
    private final Socket socket;
    private DataOutputStream outputStream;

    public OutputWriter(final Socket socket) {
        this.socket = socket;
        createOutputWriter();
    }

    public void sendMsg(final String msg) {
        try {
            outputStream.writeUTF(msg);
        } catch (EOFException | SocketException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[OutputWriter] can't write the message");
        }
    }


    private void createOutputWriter() {
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("[outputWriter] can't create a output stream");
        }
    }


}