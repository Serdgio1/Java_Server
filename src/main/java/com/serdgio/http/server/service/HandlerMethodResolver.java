package com.serdgio.http.server.service;

import com.serdgio.http.server.bind.HandlerHolder;
import com.serdgio.http.server.bind.HandlerMethod;
import com.serdgio.http.server.request.RequestContext;

public class HandlerMethodResolver {
    public HandlerMethod resolve(RequestContext context) {
        return HandlerHolder.getInstance().getHandlerMethodList()
                .stream()
                .filter(it -> context.getMethod() == it.getHttpMethod() &&
                        PathPattern.path(it.getPath()).matches(context.getPath()))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("No handler method found for request: " + context.getMethod() + " " + context.getPath());
                    return null;
                });
    }
}
