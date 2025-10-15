package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentPreferences {
    @JsonProperty("auto_bill_outstanding")
    public Boolean autoBillOutstanding;
    @JsonProperty("setup_fee")
    public Money setupFee;
    @JsonProperty("setup_fee_failure_action")
    public String setupFeeFailureAction;
    @JsonProperty("payment_failure_threshold")
    public Integer paymentFailureThreshold;
}
