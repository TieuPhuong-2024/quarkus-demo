package com.example.exception;

public class UserNotFoundException extends SubscriptionException {

    public UserNotFoundException(Long userId) {
        super("USER_NOT_FOUND", "User not found with ID: " + userId);
    }

    public UserNotFoundException(String email) {
        super("USER_NOT_FOUND", "User not found with email: " + email);
    }
}