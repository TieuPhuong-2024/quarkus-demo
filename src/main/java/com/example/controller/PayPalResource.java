package com.example.controller;

import com.example.dto.plan.CreateSubscriptionPlanRequest;
import com.example.dto.subscription.CreateSubscriptionRequest;
import com.example.dto.webhook.CreateWebhookEventTypeRequest;
import com.example.dto.webhook.PatchOperation;
import com.example.dto.webhook.SimulateEventRequest;
import com.example.dto.webhook.VerifyWebhookSignatureRequest;
import com.example.dto.webhook.WebhookEvent;
import com.example.service.PayPalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/paypal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PayPalResource {

    @Inject
    PayPalService service;

    @POST
    @Path("/subscriptions")
    public Response create(CreateSubscriptionRequest request) {
        Map<String, Object> result = service.createSubscription(request);
        return Response.ok(result).build();
    }

    @GET
    @Path("/subscriptions/{id}")
    public Response get(@PathParam("id") String id) {
        return Response.ok(service.getSubscription(id)).build();
    }

    @POST
    @Path("/billing/plans")
    public Response createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        return Response.ok(service.createSubscriptionPlan(request)).build();
    }

    @GET
    @Path("/billing/plans")
    public Response getAllPlans() {
        return Response.ok(service.getAllPlans()).build();
    }

    @POST
    @Path("/catalog/products")
    public Response createProduct(Map<String, Object> body) {
        if (body == null || !body.containsKey("name") || !body.containsKey("type")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "name and type are required")).build();
        }
        return Response.ok(service.createProduct(body)).build();
    }

    @GET
    @Path("/catalog/products/{id}")
    public Response getProduct(@PathParam("id") String id) {
        return Response.ok(service.getProduct(id)).build();
    }

    @GET
    @Path("/catalog/products")
    public Response listProducts(@QueryParam("page_size") Integer pageSize,
                                 @QueryParam("page") Integer page,
                                 @QueryParam("total_required") @DefaultValue("true") boolean totalRequired) {
        return Response.ok(service.listProducts(pageSize, page, totalRequired)).build();
    }

    // Webhooks
    @POST
    @Path("/webhooks")
    public Response createWebhook(CreateWebhookEventTypeRequest request) {
        return Response.ok(service.createWebhook(request)).build();
    }

    @GET
    @Path("/webhooks")
    public Response listWebhooks() {
        return Response.ok(service.listWebhooks()).build();
    }

    @GET
    @Path("/webhooks/{webhook_id}")
    public Response getWebhook(@PathParam("webhook_id") String webhookId) {
        return Response.ok(service.getWebhook(webhookId)).build();
    }

    @PATCH
    @Path("/webhooks/{webhook_id}")
    public Response updateWebhook(@PathParam("webhook_id") String webhookId, List<PatchOperation> patches) {
        return Response.ok(service.updateWebhook(webhookId, patches)).build();
    }

    @DELETE
    @Path("/webhooks/{webhook_id}")
    public Response deleteWebhook(@PathParam("webhook_id") String webhookId) {
        service.deleteWebhook(webhookId);
        return Response.noContent().build();
    }

    @GET
    @Path("/webhooks-event-types")
    public Response listAvailableEventTypes() {
        return Response.ok(service.listAvailableEventTypes()).build();
    }

    @GET
    @Path("/webhooks/{webhook_id}/event-types")
    public Response getWebhookEventTypes(@PathParam("webhook_id") String webhookId) {
        return Response.ok(service.getWebhookEventTypes(webhookId)).build();
    }

    @POST
    @Path("/webhooks/verify-signature")
    public Response verifySignature(VerifyWebhookSignatureRequest request) {
        return Response.ok(service.verifyWebhookSignature(request)).build();
    }

    @POST
    @Path("/webhooks/simulate-event")
    public Response simulateEvent(SimulateEventRequest request) {
        return Response.ok(service.simulateEvent(request)).build();
    }

    @GET
    @Path("/webhooks-events")
    public Response listWebhookEvents(@QueryParam("page_size") Integer pageSize,
                                      @QueryParam("start_time") String startTime,
                                      @QueryParam("end_time") String endTime,
                                      @QueryParam("transaction_id") String transactionId,
                                      @QueryParam("event_type") String eventType) {
        return Response.ok(service.listWebhookEvents(pageSize, startTime, endTime, transactionId, eventType)).build();
    }

    @GET
    @Path("/webhooks-events/{event_id}")
    public Response getWebhookEvent(@PathParam("event_id") String eventId) {
        return Response.ok(service.getWebhookEventById(eventId)).build();
    }

    @POST
    @Path("/webhooks-events/{event_id}/resend")
    public Response resendWebhookEvent(@PathParam("event_id") String eventId) {
        service.resendWebhookEvent(eventId);
        return Response.accepted().build();
    }

    @POST
    @Path("/webhooks/events")
    public Response handleWebhookEvent(WebhookEvent event) {
        service.handleWebhookEvent(event);
        return Response.ok().build();
    }
}