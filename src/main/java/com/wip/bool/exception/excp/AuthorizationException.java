package com.wip.bool.exception.excp;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        this("");
    }

    public AuthorizationException(String message) {
        this(message, null);
    }

    public AuthorizationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
