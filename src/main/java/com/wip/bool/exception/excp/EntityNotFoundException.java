package com.wip.bool.exception.excp;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode);
    }

    public EntityNotFoundException(Long id, ErrorCode errorCode) {
        this(String.format("%s id = %s", errorCode.getMessage(), id), errorCode);
    }

    public EntityNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
