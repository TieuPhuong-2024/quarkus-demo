package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SimulateEventRequest {
    private String url; // optional, if empty uses all matching
    @JsonProperty("event_type")
    private String eventType;
    @JsonProperty("resource_version")
    private String resourceVersion; // 2.0 recommended
}