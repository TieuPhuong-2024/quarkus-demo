package com.example.dto.subscription;

import com.example.util.FlexibleDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayPalSubscriptionResponse {

    private String status;
    private String id;
    @JsonProperty("create_time")
    @JsonDeserialize(using = FlexibleDateTimeDeserializer.class)
    private LocalDateTime createTime;
    private List<Link> links;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private String href;
        private String rel;
        private String method;
    }
}
