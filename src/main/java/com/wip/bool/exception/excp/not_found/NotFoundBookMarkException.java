package com.wip.bool.exception.excp.not_found;

public class NotFoundBookMarkException extends RuntimeException {

    public NotFoundBookMarkException() {
        this("즐겨찾기가 존재하지 않습니다.");
    }

    public NotFoundBookMarkException(Long id) {
        this(String.format("즐겨찾기가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundBookMarkException(String message) {
        this(message, null);
    }

    public NotFoundBookMarkException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
