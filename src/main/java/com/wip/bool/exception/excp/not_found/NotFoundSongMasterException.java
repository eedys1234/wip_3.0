package com.wip.bool.exception.excp.not_found;

public class NotFoundSongMasterException extends RuntimeException {

    public NotFoundSongMasterException() {
        this("분류가 존재하지 않습니다.");
    }

    public NotFoundSongMasterException(Long id) {
        this(String.format("분류가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundSongMasterException(String message) {
        this(message, null);
    }

    public NotFoundSongMasterException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
