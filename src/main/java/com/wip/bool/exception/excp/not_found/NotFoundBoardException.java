package com.wip.bool.exception.excp.not_found;

public class NotFoundBoardException extends RuntimeException {

    public NotFoundBoardException() {
        this("게시물이 존재하지 않습니다.");
    }

    public NotFoundBoardException(Long id) {
        this(String.format("게시물이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundBoardException(String message) {
        this(message, null);
    }
    public NotFoundBoardException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
