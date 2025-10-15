package com.example.service;

import com.example.client.PayPalAuthClient;
import com.example.client.PayPalCatalogProductsClient;
import com.example.client.PayPalSubscriptionPlanClient;
import com.example.client.PayPalSubscriptionsClient;
import com.example.client.PayPalWebhookClient;
import com.example.config.PayPalConfig;
import com.example.dto.plan.CreateSubscriptionPlanRequest;
import com.example.dto.subscription.CreateSubscriptionRequest;
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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PayPalService {

    @Inject
    PayPalConfig cfg;

    @Inject
    @RestClient
    PayPalAuthClient authClient;

    @Inject
    @RestClient
    PayPalSubscriptionsClient subsClient;

    @Inject
    @RestClient
    PayPalCatalogProductsClient productsClient;

    @Inject
    @RestClient
    PayPalSubscriptionPlanClient plansClient;

    @Inject
    @RestClient
    PayPalWebhookClient webhookClient;

    private String bearerToken() {
        String basic = Base64.getEncoder().encodeToString(
                (cfg.clientId() + ":" + cfg.clientSecret()).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> resp = authClient.token("Basic " + basic, "client_credentials");
        String accessToken = (String) resp.get("access_token");
        return "Bearer " + accessToken;
    }

    // request đơn giản: tạo subscription từ plan_id đã có sẵn
    public Map<String, Object> createSubscription(CreateSubscriptionRequest request) {
        return subsClient.create(bearerToken(), request);
    }

    public Map<String, Object> getSubscription(String subscriptionId) {
        return subsClient.get(bearerToken(), subscriptionId);
    }

    // Catalog Products
    public Map<String, Object> createProduct(Map<String, Object> request) {
        return productsClient.create(bearerToken(), request);
    }

    public Map<String, Object> getProduct(String id) {
        return productsClient.get(bearerToken(), id);
    }

    public Map<String, Object> listProducts(Integer pageSize, Integer page, Boolean totalRequired) {
        return productsClient.list(bearerToken(), pageSize, page, totalRequired);
    }

    public Map<String, Object> createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        return plansClient.create(bearerToken(), request);
    }

    public Map<String, Object> getAllPlans() {
        return plansClient.getAll(bearerToken());
    }

    public Map<String, Object> createWebhook(CreateWebhookEventTypeRequest request) {
        return webhookClient.createWebhook(bearerToken(), request);
    }

    public WebhookEventTypesListResponse getWebhookEventTypes(String webhookId) {
        return webhookClient.getWebhookEventTypes(bearerToken(), webhookId);
    }

    // Webhooks SDK additions
    public ListWebhooksResponse listWebhooks() {
        return webhookClient.listWebhooks(bearerToken());
    }

    public Webhook getWebhook(String webhookId) {
        return webhookClient.getWebhook(bearerToken(), webhookId);
    }

    public Webhook updateWebhook(String webhookId, List<PatchOperation> patches) {
        return webhookClient.updateWebhook(bearerToken(), webhookId, patches);
    }

    public void deleteWebhook(String webhookId) {
        webhookClient.deleteWebhook(bearerToken(), webhookId);
    }

    public WebhookEventTypesListResponse listAvailableEventTypes() {
        return webhookClient.listAvailableEventTypes(bearerToken());
    }

    public VerifyWebhookSignatureResponse verifyWebhookSignature(VerifyWebhookSignatureRequest request) {
        return webhookClient.verifySignature(bearerToken(), request);
    }

    public SimulateEventResponse simulateEvent(SimulateEventRequest request) {
        return webhookClient.simulateEvent(bearerToken(), request);
    }

    public ListWebhookEventsResponse listWebhookEvents(Integer pageSize, String startTime, String endTime,
                                                       String transactionId, String eventType) {
        return webhookClient.listWebhookEvents(bearerToken(), pageSize, startTime, endTime, transactionId, eventType);
    }

    public WebhookEvent getWebhookEventById(String eventId) {
        return webhookClient.getWebhookEvent(bearerToken(), eventId);
    }

    public void resendWebhookEvent(String eventId) {
        webhookClient.resendWebhookEvent(bearerToken(), eventId);
    }

    public void handleWebhookEvent(WebhookEvent event) {
        // Process the incoming webhook event
        System.out.println("Received webhook event: " + event.getEventType() + " - " + event.getSummary());

        try {
            switch (event.getEventType()) {
                case "BILLING.SUBSCRIPTION.CREATED":
                    break;
                case "BILLING.SUBSCRIPTION.ACTIVATED":
                    break;
                case "PAYMENT.SALE.COMPLETED":
                    break;
                case "BILLING.SUBSCRIPTION.CANCELLED":
                    break;
                case "BILLING.SUBSCRIPTION.SUSPENDED":
                    break;
                case "BILLING.SUBSCRIPTION.EXPIRED":
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getEventType());
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error processing webhook event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
