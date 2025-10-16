package com.example.dto.subscription;

import com.example.dto.Money;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviseSubscriptionRequest {

    @JsonProperty("plan_id")
    public String planId;

    public String quantity;

    @JsonProperty("shipping_amount")
    public Money shippingAmount;

    @JsonProperty("effective_date")
    public String effectiveDate;             // ISO 8601

    @JsonProperty("application_context")
    public ApplicationContext applicationContext;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApplicationContext {
        @JsonProperty("brand_name")
        public String brandName;

        public String locale;

        @JsonProperty("shipping_preference")
        public String shippingPreference;    // GET_FROM_FILE | NO_SHIPPING | SET_PROVIDED_ADDRESS

        @JsonProperty("user_action")
        public String userAction;            // SUBSCRIBE_NOW | CONTINUE

        @JsonProperty("payment_method")
        public PaymentMethod paymentMethod;

        @JsonProperty("return_url")
        public String returnUrl;

        @JsonProperty("cancel_url")
        public String cancelUrl;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentMethod {
        @JsonProperty("payer_selected")
        public String payerSelected;

        @JsonProperty("payee_preferred")
        public String payeePreferred;
    }
}