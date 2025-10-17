package com.example.resource;

import com.example.dto.ApiResponse;
import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.service.SubscriptionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

@Path("/api/v1/subscriptions")
public class SubscriptionResource {

    @Inject
    SubscriptionService subscriptionService;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public ApiResponse<CreatePayPalSubscriptionResponse> create(@HeaderParam("X-Crochet-Access-Token") String token,
                                                                 CreateSubscriptionRequest request) {
        CreatePayPalSubscriptionResponse response = subscriptionService.create(token, request);
        return ApiResponse.success("Subscription created successfully", response);
    }

    @GET
    @Path("/return")
    public ApiResponse<Void> handleSubscriptionReturn(@QueryParam("subscription_id") String subscriptionId) {
        subscriptionService.handleSubscriptionReturn(subscriptionId);
        return ApiResponse.success("Subscription activated successfully for ID: " + subscriptionId);
    }

    @GET
    @Path("/cancel")
    public ApiResponse<Void> handleSubscriptionCancel(@QueryParam("subscription_id") String subscriptionId) {
        return ApiResponse.success("Subscription cancellation acknowledged for ID: " + subscriptionId);
    }

    @POST
    @Path("/webhook")
    public ApiResponse<Void> handleWebhook(String payload) {
        subscriptionService.handleWebhookEvent(payload);
        return ApiResponse.success("Webhook processed successfully");
    }
}
