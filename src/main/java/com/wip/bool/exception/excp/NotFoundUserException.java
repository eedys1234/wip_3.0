package com.wip.bool.exception.excp;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        this("사용자가 존재하지 않습니다.");
    }

    public NotFoundUserException(String message) {
        this(message, null);
    }

    public NotFoundUserException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
