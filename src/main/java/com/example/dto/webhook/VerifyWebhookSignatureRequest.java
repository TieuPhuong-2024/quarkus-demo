package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyWebhookSignatureRequest {
    @JsonProperty("transmission_id")
    private String transmissionId;
    @JsonProperty("transmission_time")
    private String transmissionTime;
    @JsonProperty("cert_url")
    private String certUrl;
    @JsonProperty("auth_algo")
    private String authAlgo;
    @JsonProperty("transmission_sig")
    private String transmissionSig;
    @JsonProperty("webhook_id")
    private String webhookId;
    @JsonProperty("webhook_event")
    private Object webhookEvent;
}