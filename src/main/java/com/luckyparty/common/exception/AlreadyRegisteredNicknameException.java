package com.luckyparty.common.exception;

public class AlreadyRegisteredNicknameException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "이미 가입된 닉네임입니다.";

    public AlreadyRegisteredNicknameException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyRegisteredNicknameException(String message) {
        super(message);
    }

    public AlreadyRegisteredNicknameException(String message, Throwable cause) {
        super(message, cause);
    }
}
