package com.example.service;

import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.entity.*;
import com.example.exception.SubscriptionException;
import com.example.exception.SubscriptionNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.repository.SubscriptionRepository;
import com.example.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class SubscriptionManagementService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionManagementService.class);

    @Inject
    SubscriptionRepository subscriptionRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    PayPalService payPalService;

    /**
     * Creates a new subscription for a user
     */
    @Transactional
    public Subscription createSubscriptionForUser(Long userId, CreateSubscriptionRequest request) {
        logger.info("Creating subscription for user: {}", userId);

        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        // Check if user already has an active subscription
        if (user.hasActiveSubscription()) {
            throw new IllegalStateException("User already has an active subscription");
        }

        try {
            // Create subscription via PayPal
            Map<String, Object> payPalResponse = payPalService.createSubscription(request);

            // Extract subscription ID from PayPal response
            @SuppressWarnings("unchecked")
            Map<String, Object> subscriptionData = (Map<String, Object>) payPalResponse;
            String paypalSubscriptionId = (String) subscriptionData.get("id");

            if (paypalSubscriptionId == null) {
                throw new RuntimeException("Failed to get subscription ID from PayPal response");
            }

            // Create local subscription record
            Subscription subscription = Subscription.builder()
                    .user(user)
                    .paypalSubscriptionId(paypalSubscriptionId)
                    .planId(request.planId)
                    .status(SubscriptionStatus.APPROVAL_PENDING)
                    .quantity(request.quantity != null ? request.quantity : "1")
                    .startTime(request.startTime != null ?
                        LocalDateTime.parse(request.startTime) : null)
                    .customId(request.customId)
                    .build();

            subscriptionRepository.persist(subscription);
            logger.info("Created subscription record: {} for user: {}", paypalSubscriptionId, userId);

            return subscription;

        } catch (Exception e) {
            logger.error("Failed to create subscription for user {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to create subscription", e);
        }
    }

    /**
     * Gets user's active subscription
     */
    public Optional<Subscription> getUserActiveSubscription(Long userId) {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
        return activeSubscriptions.isEmpty() ? Optional.empty() : Optional.of(activeSubscriptions.get(0));
    }

    /**
     * Gets all subscriptions for a user
     */
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    /**
     * Checks if user has an active subscription
     */
    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.countByUserAndStatus(getUserById(userId), SubscriptionStatus.ACTIVE) > 0;
    }

    /**
     * Gets subscription by PayPal subscription ID
     */
    public Optional<Subscription> getSubscriptionByPayPalId(String paypalSubscriptionId) {
        List<Subscription> subscriptions = subscriptionRepository.findByPaypalSubscriptionId(paypalSubscriptionId);
        return subscriptions.isEmpty() ? Optional.empty() : Optional.of(subscriptions.get(0));
    }

    /**
     * Cancels a user's subscription
     */
    @Transactional
    public boolean cancelSubscription(Long userId, Long subscriptionId) {
        logger.info("Cancelling subscription {} for user: {}", subscriptionId, userId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription == null) {
            throw new SubscriptionNotFoundException(subscriptionId);
        }

        // Verify ownership
        if (!subscription.getUser().getId().equals(userId)) {
            throw new SubscriptionException("SUBSCRIPTION_OWNERSHIP_ERROR",
                "Subscription does not belong to user");
        }

        if (subscription.isCancelled()) {
            logger.warn("Subscription {} is already cancelled", subscriptionId);
            return false;
        }

        try {
            // Cancel in PayPal (you would need to implement this in PayPalService)
            // payPalService.cancelSubscription(subscription.getPaypalSubscriptionId());

            // Update local status
            subscription.setStatus(SubscriptionStatus.CANCELLED);
            subscription.setCancelledAt(LocalDateTime.now());
            subscription.setUpdatedAt(LocalDateTime.now());
            subscriptionRepository.persist(subscription);

            logger.info("Successfully cancelled subscription: {}", subscriptionId);
            return true;

        } catch (Exception e) {
            logger.error("Failed to cancel subscription {}: {}", subscriptionId, e.getMessage(), e);
            throw new RuntimeException("Failed to cancel subscription", e);
        }
    }

    /**
     * Validates if a user has access to premium features
     */
    public boolean hasAccessToFeature(Long userId, String feature) {
        Optional<Subscription> activeSubscription = getUserActiveSubscription(userId);
        if (activeSubscription.isEmpty()) {
            return false;
        }

        // Here you could implement feature-based access control
        // For now, any active subscription grants access to all features
        return activeSubscription.get().isActive();
    }

    /**
     * Gets subscription statistics for a user
     */
    public SubscriptionStats getUserSubscriptionStats(Long userId) {
        User user = getUserById(userId);
        List<Subscription> subscriptions = subscriptionRepository.findByUser(user);

        long activeCount = subscriptions.stream()
                .mapToLong(s -> s.isActive() ? 1 : 0)
                .sum();

        long totalCount = subscriptions.size();

        return new SubscriptionStats(totalCount, activeCount);
    }

    /**
     * Associates a subscription with a user (for webhook processing)
     */
    @Transactional
    public void associateSubscriptionWithUser(String paypalSubscriptionId, Long userId) {
        logger.info("Associating subscription {} with user: {}", paypalSubscriptionId, userId);

        User user = getUserById(userId);

        List<Subscription> subscriptions = subscriptionRepository.findByPaypalSubscriptionId(paypalSubscriptionId);
        if (subscriptions.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found: " + paypalSubscriptionId);
        }

        Subscription subscription = subscriptions.get(0);
        subscription.setUser(user);
        subscriptionRepository.persist(subscription);

        logger.info("Associated subscription {} with user: {}", paypalSubscriptionId, userId);
    }

    /**
     * Processes subscription activation after payment approval
     */
    @Transactional
    public void activateSubscription(String paypalSubscriptionId) {
        logger.info("Activating subscription: {}", paypalSubscriptionId);

        List<Subscription> subscriptions = subscriptionRepository.findByPaypalSubscriptionId(paypalSubscriptionId);
        if (subscriptions.isEmpty()) {
            logger.warn("Subscription not found for activation: {}", paypalSubscriptionId);
            return;
        }

        Subscription subscription = subscriptions.get(0);
        if (subscription.getStatus() == SubscriptionStatus.ACTIVE) {
            logger.info("Subscription {} is already active", paypalSubscriptionId);
            return;
        }

        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setUpdatedAt(LocalDateTime.now());
        subscriptionRepository.persist(subscription);

        logger.info("Successfully activated subscription: {}", paypalSubscriptionId);
    }

    // Helper methods
    private User getUserById(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        return user;
    }

    /**
     * Subscription statistics data class
     */
    public static class SubscriptionStats {
        private final long totalSubscriptions;
        private final long activeSubscriptions;

        public SubscriptionStats(long totalSubscriptions, long activeSubscriptions) {
            this.totalSubscriptions = totalSubscriptions;
            this.activeSubscriptions = activeSubscriptions;
        }

        public long getTotalSubscriptions() { return totalSubscriptions; }
        public long getActiveSubscriptions() { return activeSubscriptions; }
    }
}