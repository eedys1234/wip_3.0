package com.wip.bool.exception.excp;

public class AuthorizationException extends BusinessException {

    public AuthorizationException() {
        this(ErrorCode.AUTHORIZATION);
    }

    public AuthorizationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
