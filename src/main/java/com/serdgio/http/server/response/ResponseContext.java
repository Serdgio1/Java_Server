package com.serdgio.http.server.response;

import com.serdgio.http.server.common.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ResponseContext {
    private HttpStatus status;

    public static ResponseContext build(HttpStatus status) {
        var context = new ResponseContext();
        context.setStatus(status);
        return context;
    }

    public byte[] getResponseAsBytes() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byteArrayOutputStream.write(("HTTP/1.1" + status.getCode() + " " + status.getReasonPhrase() + "\r\n").getBytes());
            //TODO: headers
            byteArrayOutputStream.write("\r\n".getBytes());
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
