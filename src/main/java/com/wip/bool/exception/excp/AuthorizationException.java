package com.wip.bool.exception.excp;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        this("권한이 존재하지 않습니다.");
    }

    public AuthorizationException(String message) {
        this(message, null);
    }

    public AuthorizationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
