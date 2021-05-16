package com.wip.bool.exception.excp.not_found;

public class NotFoundUserBoxException extends RuntimeException {

    public NotFoundUserBoxException() {
        this("사용자 Box 정보가 없습니다.");
    }

    public NotFoundUserBoxException(Long id) {
        this(String.format("사용자 Box 정보가 없습니다. id = %s", id));
    }

    public NotFoundUserBoxException(String message) {
        this(message, null);
    }

    public NotFoundUserBoxException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
