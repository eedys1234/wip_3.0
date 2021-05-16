package com.wip.bool.exception.excp.not_found;

public class NotFoundGroupException extends RuntimeException {

    public NotFoundGroupException() {
        this("그룹이 존재하지 않습니다.");
    }

    public NotFoundGroupException(Long id) {
        this(String.format("그룹이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundGroupException(String message) {
        this(message, null);
    }

    public NotFoundGroupException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
