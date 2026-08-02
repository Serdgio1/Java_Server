package com.serdgio.http.server.bind;

import com.serdgio.http.server.common.HttpMethod;
import com.serdgio.http.server.exception.HandlerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object handlerObject;

    private final HttpMethod httpMethod;

    private final String path;

    private final Method handler;


    public HandlerMethod(Object handlerObject, HttpMethod httpMethod, String path, Method handler) {
        this.handlerObject = handlerObject;
        this.httpMethod = httpMethod;
        this.path = path;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    public <T> T invoke(Object... args) {
        try {
            return (T) handler.invoke(handlerObject, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new HandlerException(e);
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }
}
