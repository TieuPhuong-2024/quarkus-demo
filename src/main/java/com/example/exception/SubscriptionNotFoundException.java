package com.example.exception;

public class SubscriptionNotFoundException extends SubscriptionException {

    public SubscriptionNotFoundException(String subscriptionId) {
        super("SUBSCRIPTION_NOT_FOUND", "Subscription not found: " + subscriptionId);
    }

    public SubscriptionNotFoundException(Long subscriptionId) {
        super("SUBSCRIPTION_NOT_FOUND", "Subscription not found with ID: " + subscriptionId);
    }
}