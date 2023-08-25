package com.after.life.chatai.exception;

/**
 * @Author Lostceleste
 * @Version 1.0
 * @Date 2023-08-15 16:21
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
