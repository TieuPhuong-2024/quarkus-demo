package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyWebhookSignatureResponse {
    @JsonProperty("verification_status")
    private String verificationStatus; // SUCCESS or FAILURE
}