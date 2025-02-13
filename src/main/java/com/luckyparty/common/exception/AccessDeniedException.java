package com.luckyparty.common.exception;

public class AccessDeniedException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "올바르지 않은 토큰입니다.";

    public AccessDeniedException() {
        super(DEFAULT_MESSAGE);
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
