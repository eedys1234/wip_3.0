package com.wip.bool.exception.excp.not_found;

public class NotFoundPositionException extends RuntimeException {

    public NotFoundPositionException() {
        this("직위가 존재하지 않습니다.");
    }

    public NotFoundPositionException(Long id) {
        this(String.format("직위가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundPositionException(String message) {
        this(message, null);
    }

    public NotFoundPositionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
