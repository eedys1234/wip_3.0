package com.wip.bool.exception.excp.not_found;

public class NotFoundAppVersionException extends RuntimeException {

    public NotFoundAppVersionException() {
        this("App 정보가 존재하지 않습니다.");
    }

    public NotFoundAppVersionException(Long id) {
        this(String.format("App 정보가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundAppVersionException(String message) {
        this(message, null);
    }

    public NotFoundAppVersionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
