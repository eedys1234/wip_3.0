package com.wip.bool.exception.excp.not_found;

public class NotFoundUserException extends RuntimeException {

    public NotFoundUserException() {
        this("사용자가 존재하지 않습니다.");
    }

    public NotFoundUserException(Long id) {
        this(String.format("사용자가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundUserException(String message) {
        this(message, null);
    }

    public NotFoundUserException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
