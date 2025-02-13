package com.luckyparty.common.exception;

public class ParseTokenException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "토큰 parse에 실패하였습니다.";

    public ParseTokenException() {
        super(DEFAULT_MESSAGE);
    }

    public ParseTokenException(String message) {
        super(message);
    }

    public ParseTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
