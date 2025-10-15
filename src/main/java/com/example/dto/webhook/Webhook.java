package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Webhook {
    private String id;
    private String url;
    @JsonProperty("event_types")
    private List<EventType> eventTypes;
    private String name;
    @JsonProperty("links")
    private List<com.example.dto.Link> links;
}