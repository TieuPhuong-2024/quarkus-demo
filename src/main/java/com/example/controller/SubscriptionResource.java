package com.example.controller;

import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.entity.Subscription;
import com.example.service.SubscriptionManagementService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/api/subscriptions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SubscriptionResource {

    @Inject
    SubscriptionManagementService subscriptionService;

    /**
     * Creates a new subscription for the authenticated user
     */
    @POST
    @Path("/user/{userId}")
    public Response createSubscription(@PathParam("userId") Long userId,
                                     CreateSubscriptionRequest request) {
        try {
            Subscription subscription = subscriptionService.createSubscriptionForUser(userId, request);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of(
                        "success", true,
                        "subscriptionId", subscription.getId(),
                        "paypalSubscriptionId", subscription.getPaypalSubscriptionId(),
                        "status", subscription.getStatus().toString(),
                        "message", "Subscription created successfully. User will be redirected to PayPal for payment."
                    ))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                        "success", false,
                        "error", e.getMessage()
                    ))
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of(
                        "success", false,
                        "error", e.getMessage()
                    ))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to create subscription: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Gets the active subscription for a user
     */
    @GET
    @Path("/user/{userId}/active")
    public Response getActiveSubscription(@PathParam("userId") Long userId) {
        try {
            Optional<Subscription> subscription = subscriptionService.getUserActiveSubscription(userId);

            if (subscription.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of(
                            "success", false,
                            "error", "No active subscription found for user"
                        ))
                        .build();
            }

            return Response.ok(Map.of(
                "success", true,
                "subscription", Map.of(
                    "id", subscription.get().getId(),
                    "paypalSubscriptionId", subscription.get().getPaypalSubscriptionId(),
                    "status", subscription.get().getStatus().toString(),
                    "planId", subscription.get().getPlanId(),
                    "startTime", subscription.get().getStartTime(),
                    "nextBillingDate", subscription.get().getNextBillingDate(),
                    "createdAt", subscription.get().getCreatedAt()
                )
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to get active subscription: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Gets all subscriptions for a user
     */
    @GET
    @Path("/user/{userId}")
    public Response getUserSubscriptions(@PathParam("userId") Long userId) {
        try {
            List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);

            return Response.ok(Map.of(
                "success", true,
                "subscriptions", subscriptions.stream().map(this::subscriptionToMap).toList(),
                "count", subscriptions.size()
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to get user subscriptions: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Checks if user has access to premium features
     */
    @GET
    @Path("/user/{userId}/access/{feature}")
    public Response checkFeatureAccess(@PathParam("userId") Long userId,
                                     @PathParam("feature") String feature) {
        try {
            boolean hasAccess = subscriptionService.hasAccessToFeature(userId, feature);

            return Response.ok(Map.of(
                "success", true,
                "hasAccess", hasAccess,
                "feature", feature
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to check feature access: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Cancels a user's subscription
     */
    @DELETE
    @Path("/user/{userId}/{subscriptionId}")
    public Response cancelSubscription(@PathParam("userId") Long userId,
                                     @PathParam("subscriptionId") Long subscriptionId) {
        try {
            boolean cancelled = subscriptionService.cancelSubscription(userId, subscriptionId);

            if (!cancelled) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of(
                            "success", false,
                            "error", "Subscription is already cancelled"
                        ))
                        .build();
            }

            return Response.ok(Map.of(
                "success", true,
                "message", "Subscription cancelled successfully"
            )).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of(
                        "success", false,
                        "error", e.getMessage()
                    ))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to cancel subscription: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Gets subscription statistics for a user
     */
    @GET
    @Path("/user/{userId}/stats")
    public Response getSubscriptionStats(@PathParam("userId") Long userId) {
        try {
            SubscriptionManagementService.SubscriptionStats stats =
                subscriptionService.getUserSubscriptionStats(userId);

            return Response.ok(Map.of(
                "success", true,
                "stats", Map.of(
                    "totalSubscriptions", stats.getTotalSubscriptions(),
                    "activeSubscriptions", stats.getActiveSubscriptions()
                )
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to get subscription stats: " + e.getMessage()
                    ))
                    .build();
        }
    }

    /**
     * Checks if user has an active subscription (simple boolean response)
     */
    @GET
    @Path("/user/{userId}/has-active")
    public Response hasActiveSubscription(@PathParam("userId") Long userId) {
        try {
            boolean hasActive = subscriptionService.hasActiveSubscription(userId);

            return Response.ok(Map.of(
                "success", true,
                "hasActiveSubscription", hasActive
            )).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of(
                        "success", false,
                        "error", "Failed to check subscription status: " + e.getMessage()
                    ))
                    .build();
        }
    }

    // Helper method to convert Subscription entity to Map
    private Map<String, Object> subscriptionToMap(Subscription subscription) {
        return Map.of(
            "id", subscription.getId(),
            "paypalSubscriptionId", subscription.getPaypalSubscriptionId(),
            "status", subscription.getStatus().toString(),
            "planId", subscription.getPlanId(),
            "quantity", subscription.getQuantity(),
            "startTime", subscription.getStartTime(),
            "nextBillingDate", subscription.getNextBillingDate(),
            "lastPaymentDate", subscription.getLastPaymentDate(),
            "createdAt", subscription.getCreatedAt(),
            "updatedAt", subscription.getUpdatedAt()
        );
    }
}