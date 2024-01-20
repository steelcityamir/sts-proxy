package com.stsproxy.api.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}
