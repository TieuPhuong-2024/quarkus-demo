package com.example.service;

import com.example.service.client.PayPalAuthClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SubscriptionServiceImpl implements SubscriptionService {

    @Inject
    PayPalAuthClientService authClientService;
}
