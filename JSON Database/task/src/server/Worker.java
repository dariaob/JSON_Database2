package server;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import server.command.CommandExecutor;
import server.command.DeleteCommand;
import server.command.GetCommand;
import server.command.SetCommand;
import server.requests.Request;
import server.requests.Response;
import server.util.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;


public class Worker extends Thread {
    InputReader in;
    OutputWriter out;
    Socket socket;
    ServerSocket serverSocket;
    private boolean flag = false;
    JsonArray datalist = new JsonArray();


    public Worker (Socket socket, InputReader in, OutputWriter outputWriter, ServerSocket serverSocket){
        this.socket = socket;
        this.in = in;
        this.out = outputWriter;
        this.serverSocket = serverSocket;
    }


    @Override
    public void run() {
        CommandExecutor commandExecutor = new CommandExecutor();
        String command;
        while (!flag){
            try {
                Request request = new Gson().fromJson(in.read().trim(), Request.class);
                Response response = new Response();
                command = request.getType();
                try {
                    switch (command) {
                        case "get":
                            GetCommand getCommand = new GetCommand(request.getKey());
                            commandExecutor.executeCommand(getCommand);
                            response.setValue(getCommand.getResult().toString());
                            break;
                        case "set":
                            SetCommand setCommand = new SetCommand(request.getKey(), request.getValue());
                            commandExecutor.executeCommand(setCommand);
                            break;
                        case "delete":
                            DeleteCommand deleteCommand = new DeleteCommand(request.getKey());
                            commandExecutor.executeCommand(deleteCommand);
                            break;
                        case "exit":
                            response.setResponse(Response.STATUS_OK);
                            out.sendMsg(response.toJSON());
                            serverSocket.close();
                        default:
                            throw new NoSuchElementException();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    out.sendMsg(response.toJSON());
                    flag = true;
            }
            } catch (Exception e){
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
