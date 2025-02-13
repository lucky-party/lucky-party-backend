package com.luckyparty.common.exception.handler;

import com.luckyparty.common.exception.*;
import com.luckyparty.common.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            AlreadyRegisteredEmailException.class,
            AlreadyRegisteredNicknameException.class
    })
    public ResponseEntity<ExceptionResponse> alreadyRegisteredEmailException(Exception e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ParseTokenException.class)
    public ResponseEntity<ExceptionResponse> parseTokenException(ParseTokenException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            TokenNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> userNotFoundException(Exception e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> accessDeniedException(AccessDeniedException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> exception(Exception e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
