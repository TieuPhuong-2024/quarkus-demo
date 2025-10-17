package com.example.service;

import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;

public interface SubscriptionService {
    CreatePayPalSubscriptionResponse create(String token, CreateSubscriptionRequest request);

    void handleSubscriptionReturn(String subscriptionId);

    void handleWebhookEvent(String payload);
}
