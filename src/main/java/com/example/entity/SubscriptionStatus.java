package com.example.entity;

public enum SubscriptionStatus {
    CREATED,           // Initial state when subscription is created
    APPROVAL_PENDING,  // Waiting for user approval/payment
    ACTIVE,            // Subscription is active and paid
    SUSPENDED,         // Subscription suspended (payment failed, etc.)
    CANCELLED,         // Subscription cancelled by user or admin
    EXPIRED            // Subscription expired
}