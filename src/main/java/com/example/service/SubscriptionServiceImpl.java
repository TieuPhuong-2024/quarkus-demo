package com.example.service;

import com.example.client.PayPalSubscriptionsClient;
import com.example.dto.UpdateUserRequest;
import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.entity.SubscriptionStatus;
import com.example.exception.ResourceNotFoundException;
import com.example.exception.ValidationException;
import com.example.mapping.SubscriptionMapper;
import com.example.repository.SubscriptionRepository;
import com.example.service.client.PayPalAuthClientService;
import com.example.service.client.UserClientService;
import com.example.util.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@Slf4j
@ApplicationScoped
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Map<String, SubscriptionStatus> EVENT_TO_STATUS = Map.of(
            "BILLING.SUBSCRIPTION.RE-ACTIVATED", SubscriptionStatus.ACTIVE,
            "BILLING.SUBSCRIPTION.SUSPENDED", SubscriptionStatus.SUSPENDED,
            "BILLING.SUBSCRIPTION.CANCELLED", SubscriptionStatus.CANCELLED,
            "BILLING.SUBSCRIPTION.EXPIRED", SubscriptionStatus.EXPIRED
    );

    @Inject
    PayPalAuthClientService authClientService;

    @Inject
    @RestClient
    PayPalSubscriptionsClient paypalSubClient;

    @Inject
    UserClientService userClientService;

    @Inject
    SubscriptionRepository subRepo;

    @Inject
    SubscriptionMapper subMapper;

    @Inject
    ObjectMapper om;

    @Inject
    JwtUtils jwtUtils;

    @Transactional
    @Override
    public CreatePayPalSubscriptionResponse create(String authHeader, CreateSubscriptionRequest request) {
        log.info("Create subscription with token: {}", authHeader);

        var crochetJwtToken = jwtUtils.subString(authHeader);
        if (jwtUtils.isExpired(crochetJwtToken)) {
            throw new ValidationException("Authentication token has expired");
        }

        String userId = jwtUtils.extractSubject(crochetJwtToken);
        if (userId == null) {
            throw new ValidationException("Invalid or missing authentication token");
        }

        subRepo.findByUserId(userId)
                .filter(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE || sub.getStatus() == SubscriptionStatus.APPROVAL_PENDING)
                .ifPresent(sub -> {
                    String message = sub.getStatus() == SubscriptionStatus.ACTIVE
                            ? "User already has an active subscription"
                            : "User already has an approval pending subscription";
                    throw new ValidationException(message);
                });

        CreatePayPalSubscriptionResponse createSubRes = paypalSubClient.create(authClientService.getAccessToken(), request);
        if (createSubRes == null) {
            throw new ValidationException("Failed to create subscription with PayPal");
        }

        var sub = subMapper.toEntity(createSubRes);
        sub.setUserId(userId);
        sub.setCrochetJwtToken(crochetJwtToken);
        subRepo.persist(sub);

        log.info("Subscription created successfully for user: {}", userId);
        return createSubRes;
    }

    @Transactional
    @Override
    public void handleSubscriptionReturn(String subscriptionId) {
        log.info("Handle subscription return: {}", subscriptionId);

        var sub = subRepo.findBySubscriptionId(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription", subscriptionId));
        sub.setStatus(SubscriptionStatus.ACTIVE);
        subRepo.persist(sub);

        UpdateUserRequest request = new UpdateUserRequest();
        request.setId(sub.getUserId());
        request.setRole(UpdateUserRequest.RoleType.VIP_USER);
        userClientService.update("Bearer " + sub.getCrochetJwtToken(), request);

        log.info("User role updated: {}", sub.getUserId());

        subRepo.flush();
    }

    @SneakyThrows
    @Transactional
    @Override
    public void handleWebhookEvent(String payload) {
        // Parse event type and subscription ID from payload
        JsonNode jsonNode = om.readTree(payload);
        String eventType = jsonNode.get("event_type").asText();
        String subscriptionId = jsonNode.get("resource").get("id").asText();
        log.info("Webhook event received: {}, subscription ID: {}", eventType, subscriptionId);

        // Check if a subscription exists in a database
        var sub = subRepo.findBySubscriptionId(subscriptionId).orElse(null);
        if (sub == null) {
            log.warn("Subscription not found in DB: {}", subscriptionId);
            return;
        }

        // Update subscription status based on an event type
        SubscriptionStatus newStatus = EVENT_TO_STATUS.get(eventType);
        if (newStatus != null) {
            sub.setStatus(newStatus);
            subRepo.persist(sub);
            subRepo.flush();
            String action = eventType.substring(eventType.lastIndexOf('.') + 1).toLowerCase().replace("-", "");
            log.info("Subscription {}: {}", action, subscriptionId);
        } else {
            log.info("Unhandled webhook event type: {}", eventType);
        }
    }
}
