package com.wip.bool.exception.excp;

public class FileException extends BusinessException {

    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
