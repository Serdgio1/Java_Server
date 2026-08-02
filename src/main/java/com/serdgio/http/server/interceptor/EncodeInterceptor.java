package com.serdgio.http.server.interceptor;

import com.serdgio.http.server.exception.InterceptorException;
import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class EncodeInterceptor implements Interceptor {

    @Override
    public void  beforeSendRequest(RequestContext requestContext, ResponseContext responseContext) {
        var acceptEnconding = requestContext.getHeaders().getFirst("Accept-Encoding");
        if (acceptEnconding != null && !acceptEnconding.isBlank() && responseContext.getBodyResponse() != null) {
            var parts = acceptEnconding.split(",");
            Arrays.stream(parts)
                            .filter(it -> it.trim().equalsIgnoreCase("gzip"))
                            .findFirst()
                            .ifPresent(gzipString -> {
                                byte[] responseBody = compressResponseBody(responseContext.getResponseAsBytes());
                                responseContext.getHeaders().set("Content-length", String.valueOf(responseBody.length));
                                responseContext.getHeaders().set("Content-Encoding", "gzip");
                                responseContext.setBodyResponse(responseBody);
                            });
        }

    }

    private byte[] compressResponseBody(byte[] responseBody) {
        try {
            var outputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                gzipOutputStream.write(responseBody);
            }

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new InterceptorException("Failed to compress response body", e);
        }
    }
}
