package com.wip.bool.exception.excp.not_found;

public class NotFoundMP3Exception extends RuntimeException {

    public NotFoundMP3Exception() {
        this("MP3 파일이 존재하지 않습니다.");
    }

    public NotFoundMP3Exception(Long id) {
        this(String.format("MP3 파일이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundMP3Exception(String message) {
        this(message , null);
    }

    public NotFoundMP3Exception(String message, Throwable throwable) {
        super(message, throwable);
    }
}
