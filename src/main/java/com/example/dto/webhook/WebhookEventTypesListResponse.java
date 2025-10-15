package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WebhookEventTypesListResponse {
    @JsonProperty("event_types")
    private List<EventType> eventTypes;
}
