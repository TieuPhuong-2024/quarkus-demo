package com.example.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class SubscriptionExceptionMapper implements ExceptionMapper<SubscriptionException> {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionExceptionMapper.class);

    @Override
    public Response toResponse(SubscriptionException exception) {
        logger.error("Subscription exception occurred: {}", exception.getMessage(), exception);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", exception.getMessage());
        errorResponse.put("errorCode", exception.getErrorCode());
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        // Determine HTTP status code based on exception type
        Response.Status status = determineHttpStatus(exception);

        return Response.status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response.Status determineHttpStatus(SubscriptionException exception) {
        if (exception instanceof SubscriptionNotFoundException ||
            exception instanceof UserNotFoundException) {
            return Response.Status.NOT_FOUND;
        }

        // Add more specific status codes for other exception types
        String errorCode = exception.getErrorCode();
        switch (errorCode) {
            case "SUBSCRIPTION_ALREADY_EXISTS":
            case "USER_ALREADY_HAS_SUBSCRIPTION":
                return Response.Status.CONFLICT;
            case "INVALID_SUBSCRIPTION_STATE":
                return Response.Status.BAD_REQUEST;
            case "PAYPAL_ERROR":
                return Response.Status.BAD_GATEWAY;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}