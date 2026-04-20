package com.serdgio.http.server.interceptor;

import com.serdgio.http.server.request.RequestContext;
import com.serdgio.http.server.response.ResponseContext;

public interface Interceptor {
    void beforeSendRequest(RequestContext requestContext, ResponseContext responseContext);
}
