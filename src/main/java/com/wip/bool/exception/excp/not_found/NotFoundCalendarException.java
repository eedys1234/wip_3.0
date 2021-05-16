package com.wip.bool.exception.excp.not_found;

public class NotFoundCalendarException extends RuntimeException {

    public NotFoundCalendarException() {
        this("일정이 존재하지 않습니다.");
    }

    public NotFoundCalendarException(Long id) {
        this(String.format("일정이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundCalendarException(String message) {
        this(message, null);
    }

    public NotFoundCalendarException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
