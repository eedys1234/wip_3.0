package com.wip.bool.exception.excp.not_found;

public class NotFoundReplyException extends RuntimeException {

    public NotFoundReplyException() {
        this("댓글이 존재하지 않습니다.");
    }

    public NotFoundReplyException(Long id) {
        this(String.format("댓글이 존재하지 않습니다. id = %s", id));
    }

    public NotFoundReplyException(String message) {
        this(message, null);
    }

    public NotFoundReplyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
