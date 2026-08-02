package com.serdgio.http.server.request;

import com.serdgio.http.server.common.HttpHeaders;
import com.serdgio.http.server.common.HttpMethod;
import com.serdgio.http.server.exception.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestContext {
    private final HttpMethod httpMethod;
    private final String path;
    private final HttpHeaders headers;
    private final List<String> pathParts;
    private String body;

    public RequestContext(HttpMethod httpMethod, String path, HttpHeaders headers) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.headers = headers;
        this.pathParts = Arrays.stream(path.split("/")).toList();
    }

    public static RequestContext buildContext(BufferedReader reader) {
        try {
            var requestLine = reader.readLine();
            
            if (requestLine == null || requestLine.isBlank()) {
                return null;
            }

            var methodPath = extractMethodAndPath(requestLine);
            List<String> headers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                headers.add(line);
            }

            var httpHeader = HttpHeaders.fromHeaderList(headers);
            var requestContext = new RequestContext(methodPath.getKey(), methodPath.getValue(), httpHeader);

            var contentLength = httpHeader.getFirst("Content-Length");
            if (contentLength != null) {
                int bodySize = Integer.parseInt(contentLength);
                char[] body = new char[bodySize];
                reader.read(body);
                requestContext.setBody(new String(body));
            }

            return requestContext;
        } catch (IOException e) {
            System.out.println("Exception on build request context");
            throw new RequestException(e);
        }
    }

    private static AbstractMap.SimpleEntry<HttpMethod, String> extractMethodAndPath(String requestLine) {
        var parts = requestLine.split(" ");
        return new AbstractMap.SimpleEntry<>(HttpMethod.fromType(parts[0]), parts[1]);
    }

    public boolean hasPath() {
        return path != null && !path.isBlank();
    }

    public String getPart(int idx) {
        if (pathParts.size() <= idx) {
            return null;
        }
        return pathParts.get(idx);
    }

    public boolean pathIsEqualsTo(String actualPath) {
        return hasPath() && path.equals(actualPath);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getPath() {
        return path;
    }

    public String getLastPart() {
        return pathParts.get(pathParts.size() - 1);
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                '}';
    }
}
