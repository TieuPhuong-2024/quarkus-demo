package com.example.exception;

import com.example.dto.ApiResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        String traceId = UUID.randomUUID().toString();

        log.error("Exception occurred - Trace ID: {}, Message: {}", traceId, exception.getMessage());

        // Handle BusinessException (our custom exceptions)
        return switch (exception) {
            case BusinessException businessException -> handleBusinessException(businessException, traceId);


            // Handle IllegalArgumentException
            case IllegalArgumentException illegalArgumentException ->
                    handleIllegalArgumentException(exception, traceId);


            // Handle IllegalStateException
            case IllegalStateException illegalStateException -> handleIllegalStateException(exception, traceId);
            default ->

                // Handle other exceptions as internal server errors
                    handleGenericException(exception, traceId);
        };

    }

    private Response handleBusinessException(BusinessException exception, String traceId) {
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage(), exception.getErrorCode());

        // Get HTTP status from ErrorCode enum, fallback to BAD_REQUEST if not found
        ErrorCode errorCode = ErrorCode.fromCode(exception.getErrorCode());
        Response.Status status = (errorCode != null) ? errorCode.getHttpStatus() : Response.Status.BAD_REQUEST;

        return Response.status(status)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .header("X-Trace-Id", traceId)
                .build();
    }

    private Response handleIllegalArgumentException(Throwable exception, String traceId) {
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage(), "INVALID_ARGUMENT");
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .header("X-Trace-Id", traceId)
                .build();
    }

    private Response handleIllegalStateException(Throwable exception, String traceId) {
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage(), "INVALID_STATE");
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .header("X-Trace-Id", traceId)
                .build();
    }

    private Response handleGenericException(Throwable exception, String traceId) {
        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred", "INTERNAL_ERROR");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .header("X-Trace-Id", traceId)
                .build();
    }
}
