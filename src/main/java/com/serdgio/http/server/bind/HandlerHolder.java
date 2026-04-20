package com.serdgio.http.server.bind;

import com.serdgio.http.server.common.HttpMethod;
import com.serdgio.http.server.controller.ApplicationController;
import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerHolder {

    private static volatile HandlerHolder INSTANCE;

    private final List<Class<?>> handlerTypeList = new ArrayList<>();

    private final List<HandlerMethod>  handlerMethodList = new ArrayList<>();

    private HandlerHolder() {
        handlerTypeList.add(ApplicationController.class);
        collectHandlerMethods();
    }

    public static HandlerHolder getInstance() {
        if (INSTANCE == null) {
            synchronized (HandlerHolder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HandlerHolder();
                }
            }
        }
        return INSTANCE;
    }

    public List<HandlerMethod> getHandlerMethodList() {
        return handlerMethodList;
    }

    public void collectHandlerMethods() {
        handlerTypeList.forEach(handlerType -> {
            Method[] methods = handlerType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && parameterTypes[0] == RequestContext.class) {
                        if (method.getReturnType() == ResponseContext.class) {
                            var annotation = method.getAnnotation(RequestMapping.class);
                            try {
                                handlerMethodList.add(new HandlerMethod(handlerType.getConstructor().newInstance(),
                                        annotation.method(), annotation.path(), method));
                            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                                     NoSuchMethodException e) {
                                System.out.println("Exception: " + handlerType.getName());
                            }
                        }
                    }
                }
            }
        });
    }
}
