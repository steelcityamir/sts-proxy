package com.stsproxy.api.exception;

public class AwsClientException extends RuntimeException {
    public AwsClientException(String errorMessage) {
        super(errorMessage);
    }
}
