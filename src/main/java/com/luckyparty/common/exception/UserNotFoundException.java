package com.luckyparty.common.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
