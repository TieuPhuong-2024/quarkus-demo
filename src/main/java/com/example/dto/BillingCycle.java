package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingCycle {
    public Frequency frequency; // { interval_unit, interval_count }
    @JsonProperty("tenure_type")
    public String tenureType; // TRIAL | REGULAR
    public Integer sequence; // 1..n
    @JsonProperty("total_cycles")
    public Integer totalCycles; // 0 = infinite (for REGULAR)
    @JsonProperty("pricing_scheme")
    public PricingScheme pricingScheme; // fixed price
}
