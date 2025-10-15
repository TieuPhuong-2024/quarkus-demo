package com.example.service;

import com.example.dto.webhook.WebhookEvent;
import com.example.entity.PaymentStatus;
import com.example.entity.PaymentTransaction;
import com.example.entity.Subscription;
import com.example.entity.SubscriptionStatus;
import com.example.repository.PaymentTransactionRepository;
import com.example.repository.SubscriptionRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    SubscriptionRepository subscriptionRepository;

    @Inject
    PaymentTransactionRepository paymentTransactionRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public void processWebhookEvent(WebhookEvent event) {
        logger.info("Processing webhook event: {} - {}", event.getEventType(), event.getSummary());

        try {
            switch (event.getEventType()) {
                case "BILLING.SUBSCRIPTION.CREATED":
                    handleSubscriptionCreated(event);
                    break;
                case "BILLING.SUBSCRIPTION.ACTIVATED":
                    handleSubscriptionActivated(event);
                    break;
                case "PAYMENT.SALE.COMPLETED":
                    handlePaymentCompleted(event);
                    break;
                case "BILLING.SUBSCRIPTION.CANCELLED":
                    handleSubscriptionCancelled(event);
                    break;
                case "BILLING.SUBSCRIPTION.SUSPENDED":
                    handleSubscriptionSuspended(event);
                    break;
                case "BILLING.SUBSCRIPTION.EXPIRED":
                    handleSubscriptionExpired(event);
                    break;
                default:
                    logger.warn("Unhandled event type: {}", event.getEventType());
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing webhook event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process webhook event", e);
        }
    }

    @Transactional
    public void handleSubscriptionCreated(WebhookEvent event) {
        logger.info("Processing subscription created event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String subscriptionId = resource.get("id").asText();
            String status = resource.get("status").asText();

            // Check if subscription already exists
            if (subscriptionRepository.existsByPaypalSubscriptionId(subscriptionId)) {
                logger.info("Subscription {} already exists, skipping creation", subscriptionId);
                return;
            }

            // Extract subscription details
            JsonNode plan = resource.get("plan");
            String planId = plan.get("id").asText();

            // For now, we'll need the user to be identified through custom_id or other means
            // This is a simplified implementation
            String customId = getTextValue(resource, "custom_id");

            // Create subscription record with APPROVAL_PENDING status
            Subscription subscription = Subscription.builder()
                    .paypalSubscriptionId(subscriptionId)
                    .planId(planId)
                    .status(SubscriptionStatus.APPROVAL_PENDING)
                    .quantity(getTextValue(resource, "quantity", "1"))
                    .startTime(parseDateTime(getTextValue(resource, "start_time")))
                    .customId(customId)
                    .build();

            subscriptionRepository.persist(subscription);
            logger.info("Created subscription record for PayPal subscription: {}", subscriptionId);

        } catch (Exception e) {
            logger.error("Failed to process subscription created event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void handleSubscriptionActivated(WebhookEvent event) {
        logger.info("Processing subscription activated event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String subscriptionId = resource.get("id").asText();

            Subscription subscription = subscriptionRepository
                    .findByPaypalSubscriptionId(subscriptionId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (subscription == null) {
                logger.warn("Received activation event for unknown subscription: {}", subscriptionId);
                return;
            }

            // Update subscription status to ACTIVE
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setUpdatedAt(LocalDateTime.now());

            // Extract and set billing information
            JsonNode billingInfo = resource.get("billing_info");
            if (billingInfo != null) {
                JsonNode lastPayment = billingInfo.get("last_payment");
                if (lastPayment != null) {
                    JsonNode amount = lastPayment.get("amount");
                    if (amount != null) {
                        subscription.setBillingAmount(new BigDecimal(amount.get("value").asText()));
                        subscription.setCurrencyCode(amount.get("currency_code").asText());
                    }
                }

                JsonNode nextBillingTime = billingInfo.get("next_billing_time");
                if (nextBillingTime != null) {
                    subscription.setNextBillingDate(parseDateTime(nextBillingTime.asText()));
                }
            }

            subscriptionRepository.persist(subscription);
            logger.info("Activated subscription: {}", subscriptionId);

        } catch (Exception e) {
            logger.error("Failed to process subscription activated event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void handlePaymentCompleted(WebhookEvent event) {
        logger.info("Processing payment completed event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String transactionId = resource.get("id").asText();

            // Extract billing information
            JsonNode amount = resource.get("amount");
            BigDecimal paymentAmount = new BigDecimal(amount.get("value").asText());
            String currency = amount.get("currency_code").asText();

            // For subscription payments, we need to find the related subscription
            // This is a simplified approach - in practice, you'd need to correlate
            // the payment with the subscription through PayPal's subscription context
            String subscriptionId = extractSubscriptionIdFromPayment(resource);

            if (subscriptionId != null) {
                Subscription subscription = subscriptionRepository
                        .findByPaypalSubscriptionId(subscriptionId)
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (subscription != null) {
                    // Create payment transaction record
                    PaymentTransaction transaction = PaymentTransaction.builder()
                            .subscription(subscription)
                            .paypalTransactionId(transactionId)
                            .status(PaymentStatus.COMPLETED)
                            .amount(paymentAmount)
                            .currencyCode(currency)
                            .paymentDate(LocalDateTime.now())
                            .build();

                    paymentTransactionRepository.persist(transaction);

                    // Update subscription's last payment date
                    subscription.setLastPaymentDate(LocalDateTime.now());
                    subscription.setUpdatedAt(LocalDateTime.now());
                    subscriptionRepository.persist(subscription);

                    logger.info("Recorded payment transaction {} for subscription {}",
                              transactionId, subscriptionId);
                }
            }

        } catch (Exception e) {
            logger.error("Failed to process payment completed event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void handleSubscriptionCancelled(WebhookEvent event) {
        logger.info("Processing subscription cancelled event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String subscriptionId = resource.get("id").asText();

            Subscription subscription = subscriptionRepository
                    .findByPaypalSubscriptionId(subscriptionId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (subscription != null) {
                subscription.setStatus(SubscriptionStatus.CANCELLED);
                subscription.setCancelledAt(LocalDateTime.now());
                subscription.setUpdatedAt(LocalDateTime.now());
                subscriptionRepository.persist(subscription);

                logger.info("Cancelled subscription: {}", subscriptionId);
            }

        } catch (Exception e) {
            logger.error("Failed to process subscription cancelled event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void handleSubscriptionSuspended(WebhookEvent event) {
        logger.info("Processing subscription suspended event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String subscriptionId = resource.get("id").asText();

            Subscription subscription = subscriptionRepository
                    .findByPaypalSubscriptionId(subscriptionId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (subscription != null) {
                subscription.setStatus(SubscriptionStatus.SUSPENDED);
                subscription.setSuspendedAt(LocalDateTime.now());
                subscription.setUpdatedAt(LocalDateTime.now());
                subscriptionRepository.persist(subscription);

                logger.info("Suspended subscription: {}", subscriptionId);
            }

        } catch (Exception e) {
            logger.error("Failed to process subscription suspended event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void handleSubscriptionExpired(WebhookEvent event) {
        logger.info("Processing subscription expired event");

        try {
            JsonNode resource = objectMapper.valueToTree(event.getResource());
            String subscriptionId = resource.get("id").asText();

            Subscription subscription = subscriptionRepository
                    .findByPaypalSubscriptionId(subscriptionId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (subscription != null) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscription.setExpiredAt(LocalDateTime.now());
                subscription.setUpdatedAt(LocalDateTime.now());
                subscriptionRepository.persist(subscription);

                logger.info("Expired subscription: {}", subscriptionId);
            }

        } catch (Exception e) {
            logger.error("Failed to process subscription expired event: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Helper methods
    private String getTextValue(JsonNode node, String field) {
        return getTextValue(node, field, null);
    }

    private String getTextValue(JsonNode node, String field, String defaultValue) {
        JsonNode fieldNode = node.get(field);
        return fieldNode != null && !fieldNode.isNull() ? fieldNode.asText() : defaultValue;
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            logger.warn("Failed to parse datetime: {}", dateTimeStr);
            return null;
        }
    }

    private String extractSubscriptionIdFromPayment(JsonNode paymentResource) {
        // This is a simplified extraction - in practice, you'd need to
        // look at the payment's subscription context or custom_id
        JsonNode customField = paymentResource.get("custom_id");
        if (customField != null) {
            return customField.asText();
        }

        // Alternative: extract from supplementary data or other fields
        return null;
    }
}