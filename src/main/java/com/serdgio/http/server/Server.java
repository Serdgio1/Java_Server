package com.serdgio.http.server;

import com.serdgio.http.server.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private final ExecutorService executor;

    public Server(int port) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (var serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                var clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                executor.execute(new RequestHandler(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
