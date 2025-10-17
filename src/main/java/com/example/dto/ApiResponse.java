package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp;

    // Success response constructors
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now());
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null, null, LocalDateTime.now());
    }

    // Error response constructors
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message, T data, String errorCode) {
        return new ApiResponse<>(false, message, data, errorCode, LocalDateTime.now());
    }
}
