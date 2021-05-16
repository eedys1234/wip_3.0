package com.wip.bool.exception.excp.not_found;

public class NotFoundSheetException extends RuntimeException {

    public NotFoundSheetException() {
        this("악보가 존재하지 않습니다.");
    }

    public NotFoundSheetException(Long id) {
        this(String.format("악보가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundSheetException(String message) {
        this(message, null);
    }

    public NotFoundSheetException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
