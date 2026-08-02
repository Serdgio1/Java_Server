package com.serdgio.http.server;

import com.serdgio.http.server.common.ApplicationParametrs;

public class Main {
    public static void main(String[] args) {
        ApplicationParametrs.getInstance().setDirectory(args);

        int port = 8080;
        Server server = new Server(port);
        server.start();
    }
}