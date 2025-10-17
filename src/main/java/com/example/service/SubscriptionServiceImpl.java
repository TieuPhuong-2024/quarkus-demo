package com.example.service;

import com.example.client.PayPalSubscriptionsClient;
import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.entity.SubscriptionStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ValidationException;
import com.example.map.SubscriptionMapper;
import com.example.repository.SubscriptionRepository;
import com.example.service.client.PayPalAuthClientService;
import com.example.util.JwtDecoder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class SubscriptionServiceImpl implements SubscriptionService {

    @Inject
    PayPalAuthClientService authClientService;

    @Inject
    @RestClient
    PayPalSubscriptionsClient subscriptionsClient;

    @Inject
    SubscriptionRepository subscriptionRepository;

    @Inject
    SubscriptionMapper subMapper;


    @Transactional
    @Override
    public CreatePayPalSubscriptionResponse create(String token, CreateSubscriptionRequest request) {
        log.info("Create subscription with token: {}", token);

        String userId = extractAndValidateUserId(token);
        validateNoActiveOrPendingSubscription(userId);

        CreatePayPalSubscriptionResponse createSubRes = subscriptionsClient.create(authClientService.getAccessToken(), request);
        if (createSubRes == null) {
            throw new ValidationException("Failed to create subscription with PayPal");
        }

        var sub = subMapper.toEntity(createSubRes);
        sub.setUserId(userId);
        subscriptionRepository.persist(sub);

        log.info("Subscription created successfully for user: {}", userId);
        return createSubRes;
    }

    private String extractAndValidateUserId(String token) {
        String userId = JwtDecoder.getSubjectFromJwt(token);
        log.info("Extracted user ID: {}", userId);
        if (userId == null) {
            throw new ValidationException("Invalid or missing authentication token");
        }
        return userId;
    }

    private void validateNoActiveOrPendingSubscription(String userId) {
        subscriptionRepository.findByUserId(userId)
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE || sub.getStatus() == SubscriptionStatus.APPROVAL_PENDING)
                .ifPresent(sub -> {
                    String message = sub.getStatus() == SubscriptionStatus.ACTIVE
                            ? "User already has an active subscription"
                            : "User already has an approval pending subscription";
                    throw new ValidationException(message);
                });
    }

    @Transactional
    @Override
    public void handleSubscriptionReturn(String subscriptionId) {
        log.info("Handle subscription return: {}", subscriptionId);
        var sub = subscriptionRepository.findBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));
        sub.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.persist(sub);
        log.info("Subscription returned: {}", subscriptionId);
    }
}
