package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionListResponse {

    public List<SubscriptionResponse> subscriptions;

    public Pagination pagination;

    @JsonProperty("total_items")
    public Integer totalItems;

    @JsonProperty("total_pages")
    public Integer totalPages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Pagination {
        @JsonProperty("page")
        public Integer page;

        @JsonProperty("page_size")
        public Integer pageSize;

        @JsonProperty("total_items")
        public Integer totalItems;

        @JsonProperty("total_pages")
        public Integer totalPages;
    }
}