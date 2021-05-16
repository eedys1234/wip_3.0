package com.wip.bool.exception.excp.not_found;

public class NotFoundGuitarCodeException extends RuntimeException {

    public NotFoundGuitarCodeException() {
        this("기타코드가 존재하지 않습니다.");
    }

    public NotFoundGuitarCodeException(Long id) {
        this(String.format("기타코드가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundGuitarCodeException(String message) {
        this(message, null);
    }

    public NotFoundGuitarCodeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
