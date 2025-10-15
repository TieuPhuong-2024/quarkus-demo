package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListWebhookEventsResponse {
    @JsonProperty("events")
    private List<WebhookEvent> events;
    @JsonProperty("links")
    private List<com.example.dto.Link> links;
}