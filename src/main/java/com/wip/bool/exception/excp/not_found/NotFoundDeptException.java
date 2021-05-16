package com.wip.bool.exception.excp.not_found;

public class NotFoundDeptException extends RuntimeException {

    public NotFoundDeptException() {
        this("부서가 존재하지 않습니다.");
    }

    public NotFoundDeptException(Long id) {
        this(String.format("부서가 존재하지 않습니다. id = %s", id));
    }

    public NotFoundDeptException(String message) {
        this(message, null);
    }

    public NotFoundDeptException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

