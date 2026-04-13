package com.serdgio.http.server.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaders {

    private final Map<String, List<String>> headers;

    public HttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public static HttpHeaders fromHeaderList(List<String> headerList) {
        Map<String, List<String>> headersMap = new HashMap<>();
        headerList.forEach(line -> {
            int index = line.indexOf(':');
            if (index != -1) {
                String name = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                headersMap.computeIfAbsent(name.toLowerCase(), k -> new ArrayList<>()).add(value);
            }
        });

        return new HttpHeaders(headersMap);
    }

    public static HttpHeaders fromHeaderMap(Map<String, String> headerMap) {
        var httpHeader = new HttpHeaders(new HashMap<>());
        httpHeader.setAll(headerMap);
        return httpHeader;
    }

    public String getFirst(String key) {
        List<String> values = headers.get(key.toLowerCase());
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public List<String> getValues(String key) {
        return headers.get(key.toLowerCase());
    }

    public void add(String key, String value) {
        List<String> values = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
        values.add(value);
    }

    public void addAll(String key, List<String> value) {
        List<String> values = headers.computeIfAbsent(key.toLowerCase(), k -> new ArrayList<>());
        values.addAll(value);
    }

    public void addAll(Map<String, List<String>> headerMap) {
        headerMap.forEach(this::addAll);
    }

    public void set(String key, String value) {
        List<String> values = new ArrayList<>();
        values.add(value);
        headers.put(key.toLowerCase(), values);
    }

    public void setAll(Map<String, String> values) {
        values.forEach(this::set);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            sb.append(key).append(": ");

            for (int i = 0; i < values.size(); i++) {
                sb.append(values.get(i));
                if (i < values.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
