package com.luckyparty.common.exception;

public class AlreadyRegisteredEmailException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 가입된 이메일입니다.";

    public AlreadyRegisteredEmailException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyRegisteredEmailException(String message) {
        super(message);
    }

    public AlreadyRegisteredEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
