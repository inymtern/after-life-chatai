package com.after.life.chatai.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-17 20:35
 */
@RestControllerAdvice
public class EHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity.status(999).body("token is expired");
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.status(999).body("token is expired");
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<?> socketTimeoutException(SocketTimeoutException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("feign client exec  timed out");
    }
}
