package com.example.client;

import com.example.dto.webhook.CreateWebhookEventTypeRequest;
import com.example.dto.webhook.ListWebhookEventsResponse;
import com.example.dto.webhook.ListWebhooksResponse;
import com.example.dto.webhook.PatchOperation;
import com.example.dto.webhook.SimulateEventRequest;
import com.example.dto.webhook.SimulateEventResponse;
import com.example.dto.webhook.VerifyWebhookSignatureRequest;
import com.example.dto.webhook.VerifyWebhookSignatureResponse;
import com.example.dto.webhook.Webhook;
import com.example.dto.webhook.WebhookEvent;
import com.example.dto.webhook.WebhookEventTypesListResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Map;

@RegisterRestClient(configKey = "paypal")
public interface PayPalWebhookClient {

    // Base path: /v1/notifications/webhooks
    @POST
    @Path("/v1/notifications/webhooks")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> createWebhook(@HeaderParam("Authorization") String bearer,
                                      CreateWebhookEventTypeRequest request);

    @GET
    @Path("/v1/notifications/webhooks")
    @Produces(MediaType.APPLICATION_JSON)
    ListWebhooksResponse listWebhooks(@HeaderParam("Authorization") String bearer);

    @GET
    @Path("/v1/notifications/webhooks/{webhook_id}")
    @Produces(MediaType.APPLICATION_JSON)
    Webhook getWebhook(@HeaderParam("Authorization") String bearer,
                       @PathParam("webhook_id") String webhookId);

    @PATCH
    @Path("/v1/notifications/webhooks/{webhook_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Webhook updateWebhook(@HeaderParam("Authorization") String bearer,
                          @PathParam("webhook_id") String webhookId,
                          List<PatchOperation> patches);

    @DELETE
    @Path("/v1/notifications/webhooks/{webhook_id}")
    void deleteWebhook(@HeaderParam("Authorization") String bearer,
                       @PathParam("webhook_id") String webhookId);

    @GET
    @Path("/v1/notifications/webhooks-event-types")
    @Produces(MediaType.APPLICATION_JSON)
    WebhookEventTypesListResponse listAvailableEventTypes(@HeaderParam("Authorization") String bearer);

    @GET
    @Path("/v1/notifications/webhooks/{webhook_id}/event-types")
    @Produces(MediaType.APPLICATION_JSON)
    WebhookEventTypesListResponse getWebhookEventTypes(@HeaderParam("Authorization") String bearer,
                                                       @PathParam("webhook_id") String webhookId);

    @POST
    @Path("/v1/notifications/verify-webhook-signature")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    VerifyWebhookSignatureResponse verifySignature(@HeaderParam("Authorization") String bearer,
                                                   VerifyWebhookSignatureRequest request);

    @POST
    @Path("/v1/notifications/simulate-event")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    SimulateEventResponse simulateEvent(@HeaderParam("Authorization") String bearer,
                                        SimulateEventRequest request);

    @GET
    @Path("/v1/notifications/webhooks-events")
    @Produces(MediaType.APPLICATION_JSON)
    ListWebhookEventsResponse listWebhookEvents(@HeaderParam("Authorization") String bearer,
                                                @QueryParam("page_size") Integer pageSize,
                                                @QueryParam("start_time") String startTime,
                                                @QueryParam("end_time") String endTime,
                                                @QueryParam("transaction_id") String transactionId,
                                                @QueryParam("event_type") String eventType);

    @GET
    @Path("/v1/notifications/webhooks-events/{event_id}")
    @Produces(MediaType.APPLICATION_JSON)
    WebhookEvent getWebhookEvent(@HeaderParam("Authorization") String bearer,
                                 @PathParam("event_id") String eventId);

    @POST
    @Path("/v1/notifications/webhooks-events/{event_id}/resend")
    void resendWebhookEvent(@HeaderParam("Authorization") String bearer,
                            @PathParam("event_id") String eventId);
}
