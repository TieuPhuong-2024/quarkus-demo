package com.example.exception;

/**
 * Base exception for subscription-related errors
 */
public class SubscriptionException extends RuntimeException {

    private final String errorCode;

    public SubscriptionException(String message) {
        super(message);
        this.errorCode = "SUBSCRIPTION_ERROR";
    }

    public SubscriptionException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "SUBSCRIPTION_ERROR";
    }

    public SubscriptionException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SubscriptionException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}