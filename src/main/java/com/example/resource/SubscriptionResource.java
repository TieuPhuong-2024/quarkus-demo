package com.example.resource;

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
    public CreatePayPalSubscriptionResponse create(@HeaderParam("X-Crochet-Access-Token") String token,
                                                   CreateSubscriptionRequest request) {
        return subscriptionService.create(token, request);
    }

    @GET
    @Path("/return")
    public String handleSubscriptionReturn(@QueryParam("subscription_id") String subscriptionId) {
        subscriptionService.handleSubscriptionReturn(subscriptionId);
        return "OK I got subscription id here: " + subscriptionId;
    }

    @GET
    @Path("/cancel")
    public String handleSubscriptionCancel(@QueryParam("subscription_id") String subscriptionId) {
        return "OK I canceled subscription id: " + subscriptionId;
    }
}
