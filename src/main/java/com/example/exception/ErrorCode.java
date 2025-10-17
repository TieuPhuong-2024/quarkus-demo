package com.example.exception;

import jakarta.ws.rs.core.Response;

public enum ErrorCode {
    // Validation errors
    VALIDATION_ERROR("VALIDATION_ERROR", Response.Status.BAD_REQUEST),
    INVALID_ARGUMENT("INVALID_ARGUMENT", Response.Status.BAD_REQUEST),
    INVALID_STATE("INVALID_STATE", Response.Status.BAD_REQUEST),

    // Resource errors
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", Response.Status.NOT_FOUND),

    // Authentication/Authorization errors
    INVALID_TOKEN("INVALID_TOKEN", Response.Status.UNAUTHORIZED),
    UNAUTHORIZED("UNAUTHORIZED", Response.Status.UNAUTHORIZED),
    FORBIDDEN("FORBIDDEN", Response.Status.FORBIDDEN),

    // Business logic errors
    BUSINESS_RULE_VIOLATION("BUSINESS_RULE_VIOLATION", Response.Status.BAD_REQUEST),

    // External service errors
    EXTERNAL_SERVICE_ERROR("EXTERNAL_SERVICE_ERROR", Response.Status.BAD_REQUEST),

    // Internal errors
    INTERNAL_ERROR("INTERNAL_ERROR", Response.Status.INTERNAL_SERVER_ERROR);

    private final String code;
    private final Response.Status httpStatus;

    ErrorCode(String code, Response.Status httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return code;
    }

    /**
     * Get ErrorCode from string code, returns null if not found
     */
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
