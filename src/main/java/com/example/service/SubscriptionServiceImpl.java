package com.example.service;

import com.example.client.PayPalSubscriptionsClient;
import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.entity.SubscriptionStatus;
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
        
        var userId = JwtDecoder.getSubjectFromJwt(token);
        log.info("Sub: {}", userId);

        if (userId == null) {
            throw new IllegalStateException("Token is not valid");
        }

        var existingSub = subscriptionRepository.findByUserId(userId).orElse(null);
        if (existingSub != null) {
            if (existingSub.getStatus() == SubscriptionStatus.ACTIVE) {
                throw new IllegalStateException("User already has an active subscription");
            } else if (existingSub.getStatus() == SubscriptionStatus.APPROVAL_PENDING) {
                throw new IllegalStateException("User already has an approval pending subscription");
            }
        }

        var createSubRes = subscriptionsClient.create(authClientService.getAccessToken(), request);
        if (createSubRes == null) {
            throw new IllegalStateException("Create subscription failed");
        }

        var sub = subMapper.toEntity(createSubRes);
        sub.setUserId(userId);

        subscriptionRepository.persist(sub);

        return createSubRes;
    }

    @Transactional
    @Override
    public void handleSubscriptionReturn(String subscriptionId) {
        var sub = subscriptionRepository.findBySubscriptionId(subscriptionId).orElse(null);
        if (sub == null) {
            throw new IllegalStateException("Subscription not found");
        }
        sub.setStatus(SubscriptionStatus.ACTIVE);
        subscriptionRepository.persist(sub);
    }
}
