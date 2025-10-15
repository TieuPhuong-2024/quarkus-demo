package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListWebhooksResponse {
    @JsonProperty("webhooks")
    private List<Webhook> webhooks;
    @JsonProperty("links")
    private List<com.example.dto.Link> links;
}