package com.wip.bool.exception.excp.not_found;

public class NotFoundGroupMemberException extends RuntimeException {

    public NotFoundGroupMemberException() {
        this("그룹에 속해있지 않습니다.");
    }

    public NotFoundGroupMemberException(Long id) {
        this(String.format("그룹에 속해있지 않습니다. id = %s", id));
    }

    public NotFoundGroupMemberException(String message) {
        this(message, null);
    }

    public NotFoundGroupMemberException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
