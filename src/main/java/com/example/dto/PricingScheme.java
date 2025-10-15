package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingScheme {
    @JsonProperty("fixed_price")
    public Money fixedPrice;
    public Integer version;
    @JsonProperty("create_time")
    public String createTime;
    @JsonProperty("update_time")
    public String updateTime;
}
