package com.example.exception;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }

    public ValidationException(String field, String message) {
        super(String.format("Validation error for field '%s': %s", field, message), ErrorCode.VALIDATION_ERROR);
    }
}
