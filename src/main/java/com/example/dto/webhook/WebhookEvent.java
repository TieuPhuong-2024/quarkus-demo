package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WebhookEvent {
    private String id;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("resource_type")
    private String resourceType;
    @JsonProperty("event_type")
    private String eventType;
    private Object resource;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("links")
    private List<com.example.dto.Link> links;
}