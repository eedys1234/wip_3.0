package com.wip.bool.exception.excp.not_found;

public class NotFoundSongException extends RuntimeException {

    public NotFoundSongException() {
        this("곡이 존재하지 않습니다.");
    }

    public NotFoundSongException(Long id) {
        this(String.format("곡이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundSongException(String message) {
        this(message, null);
    }

    public NotFoundSongException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
