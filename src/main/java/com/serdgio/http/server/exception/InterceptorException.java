package com.serdgio.http.server.exception;

public class InterceptorException extends RuntimeException {
    public InterceptorException(String message) {
        super(message);
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterceptorException(Throwable cause) {
        super(cause);
    }

    public InterceptorException() {
    }
}
