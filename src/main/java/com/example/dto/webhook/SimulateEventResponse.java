package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SimulateEventResponse {
    @JsonProperty("id")
    private String id; // simulated event id
    @JsonProperty("event_type")
    private String eventType;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("resource_type")
    private String resourceType;
}