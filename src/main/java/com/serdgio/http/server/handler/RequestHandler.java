package com.serdgio.http.server.handler;

import com.serdgio.http.server.common.HttpStatus;
import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;
import com.serdgio.http.server.service.HandlerMethodResolver;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private final Socket clientSocket;

    private final HandlerMethodResolver handlerMethodResolver;

    public RequestHandler(Socket socket) {
        this.clientSocket = socket;
        this.handlerMethodResolver = new HandlerMethodResolver();
    }

    @Override
    public void run() {
        try {
            var inputStream = clientSocket.getInputStream();
            var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            var context = RequestContext.buildContext(bufferedReader);
            if (context == null) {
                System.out.println("Context is null!");
                return;
            }

            var os = clientSocket.getOutputStream();
            var hadlerMethod = handlerMethodResolver.resolve(context);

            if (hadlerMethod == null) {
                os.write(ResponseContext.build(HttpStatus.NOT_FOUND).getResponseAsBytes());
                os.flush();
            } else {
                ResponseContext responseContext = hadlerMethod.invoke(context);
                if (responseContext.getStatus().isError()) {
                    os.write(ResponseContext.build(responseContext.getStatus()).getResponseAsBytes());
                    os.flush();
                } else {
                    os.write(responseContext.getResponseAsBytes());
                    os.flush();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Handler exception", e);
        } finally {
            try {
                clientSocket.close();
                System.out.println("Socket closed");
            } catch (IOException e) {
                System.out.println("Exception trying to close socket");
            }
        }
    }
}