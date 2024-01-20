package com.stsproxy.api.controller;

import com.stsproxy.api.data.ApiErrorResponse;
import com.stsproxy.api.exception.AwsClientException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiErrorResponse apiError =
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed",  errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.UNAUTHORIZED);

        if (ex.getCause() != null) {
            // Means exception was re-thrown somewhere
            apiErrorResponse.setMessage(ex.getCause().getMessage());
        } else {
            apiErrorResponse.setMessage(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }

    @ExceptionHandler(AwsClientException.class)
    protected ResponseEntity<Object> handleAwsClientException(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE);

        if (ex.getCause() != null) {
            // Means exception was re-thrown somewhere
            apiErrorResponse.setMessage(ex.getCause().getMessage());
        } else {
            apiErrorResponse.setMessage(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(apiErrorResponse);
    }
}
