package com.example.dto.plan;

import com.example.dto.BillingCycle;
import com.example.dto.PaymentPreferences;
import com.example.dto.Taxes;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSubscriptionPlanRequest {
    @JsonProperty("product_id")
    public String productId;

    public String name;
    public String description;
    public String status; // e.g. ACTIVE | INACTIVE

    @JsonProperty("billing_cycles")
    public List<BillingCycle> billingCycles;

    @JsonProperty("payment_preferences")
    public PaymentPreferences paymentPreferences;

    public Taxes taxes;
}
