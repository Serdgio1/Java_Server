package com.serdgio.http.server.response;

import com.serdgio.http.server.common.HttpHeaders;
import com.serdgio.http.server.common.HttpStatus;
import com.serdgio.http.server.exception.ResponseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ResponseContext {
    private HttpStatus status;

    private HttpHeaders headers;

    private byte[] bodyResponse;

    public static ResponseContext build(HttpStatus status) {
        return build(status, null, null);
    }

    public static ResponseContext build(HttpStatus status,  HttpHeaders headers) {
        return build(status, headers, null);
    }

    public static ResponseContext build(HttpStatus status,  HttpHeaders headers, String body) {
        return buildWithBytes(status, headers, body == null ? null : body.getBytes());
    }

    public static ResponseContext buildWithBytes(HttpStatus status, HttpHeaders headers, byte[] body) {
        var context = new ResponseContext();
        context.setStatus(status);
        context.setHeaders(headers);
        context.setBodyResponse(body);
        return context;
    }

    public byte[] getResponseAsBytes() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byteArrayOutputStream.write(("HTTP/1.1 " + status.getCode() + " " + status.getReasonPhrase() + "\r\n").getBytes());

            if (headers != null) {
                byteArrayOutputStream.write(headers.toString().getBytes());
            }
            byteArrayOutputStream.write("\r\n".getBytes());

            if (bodyResponse != null) {
                byteArrayOutputStream.write(bodyResponse);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ResponseException(e);
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setBodyResponse(byte[] bodyResponse) {
        this.bodyResponse = bodyResponse;
    }
}
