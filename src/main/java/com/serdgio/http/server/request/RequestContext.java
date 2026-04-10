package com.serdgio.http.server.request;

import com.serdgio.http.server.common.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestContext {
    private final HttpMethod httpMethod;
    private final String path;
    private final List<String> pathParts;

    public RequestContext(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.pathParts = Arrays.stream(path.split("/")).toList();
    }

    public static RequestContext buildContext(BufferedReader reader) {
        try {
            var requestLine = reader.readLine();
            System.out.println("[DEBUG] Raw request line: '" + requestLine + "'");
            
            if (requestLine == null || requestLine.isBlank()) {
                System.out.println("[DEBUG] Request line is null or blank - skipping headers");
                return null;
            }

            var methodPath = extractMethodAndPath(requestLine);
            List<String> headers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                headers.add(line);
            }
            System.out.println("Headers: " + headers);
            var requestContext = new RequestContext(methodPath.getKey(), methodPath.getValue());
            return requestContext;
        } catch (IOException e) {
            System.out.println("Exception on build request context");
            throw new RuntimeException(e);
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

    @Override
    public String toString() {
        return "RequestContext{" +
                "httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                '}';
    }
}
