package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionAuthorizationResponse {

    public String id;

    public String status;

    public List<Link> links;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Link {
        public String href;
        public String rel;
        public String method;
    }
}