package com.serdgio.http.server.interceptor;

import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;

import java.util.ArrayList;
import java.util.List;

public class InterceptorHolder {

    private static volatile InterceptorHolder INSTANCE;

    private final List<Interceptor> interceptorList;

    private InterceptorHolder() {
        this.interceptorList = new ArrayList<>();
        interceptorList.add(new EncodeInterceptor());
    }

    public static InterceptorHolder getInstance() {
        if (INSTANCE == null) {
            synchronized (InterceptorHolder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InterceptorHolder();
                }
            }
        }
        return INSTANCE;
    }

    public void beforeSendRequest(RequestContext requestContext, ResponseContext responseContext) {
        interceptorList.forEach(interceptor -> interceptor.beforeSendRequest(requestContext, responseContext));
    }

}
