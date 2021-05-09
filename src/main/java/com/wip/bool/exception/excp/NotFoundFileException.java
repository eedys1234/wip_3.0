package com.wip.bool.exception.excp;

public class NotFoundFileException extends RuntimeException {

    public NotFoundFileException(String message) {
        this(message, null);
    }

    public NotFoundFileException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
