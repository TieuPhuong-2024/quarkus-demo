package com.example.dto.subscription;

import com.example.dto.Link;
import com.example.dto.Money;
import com.example.dto.PricingScheme;
import com.example.dto.Subscriber;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionResponse {

    public String id;

    public String status;

    @JsonProperty("status_change_note")
    public String statusChangeNote;

    @JsonProperty("plan_id")
    public String planId;

    public String quantity;

    @JsonProperty("create_time")
    public String createTime;

    @JsonProperty("update_time")
    public String updateTime;

    @JsonProperty("start_time")
    public String startTime;

    @JsonProperty("custom_id")
    public String customId;

    @JsonProperty("shipping_amount")
    public Money shippingAmount;

    public Subscriber subscriber;

    @JsonProperty("billing_info")
    public BillingInfo billingInfo;

    @JsonProperty("application_context")
    public ApplicationContext applicationContext;

    public List<Link> links;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BillingInfo {
        @JsonProperty("outstanding_balance")
        public Money outstandingBalance;

        @JsonProperty("cycle_executions")
        public List<CycleExecution> cycleExecutions;

        @JsonProperty("last_payment")
        public LastPayment lastPayment;

        @JsonProperty("next_billing_time")
        public String nextBillingTime;

        @JsonProperty("failed_payments_count")
        public Integer failedPaymentsCount;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CycleExecution {
        @JsonProperty("tenure_type")
        public String tenureType;

        public Integer sequence;

        @JsonProperty("cycles_completed")
        public Integer cyclesCompleted;

        @JsonProperty("cycles_remaining")
        public Integer cyclesRemaining;

        @JsonProperty("current_pricing_scheme")
        public PricingScheme currentPricingScheme;

        @JsonProperty("total_cycles")
        public Integer totalCycles;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LastPayment {
        public String time;

        public Money amount;

        @JsonProperty("payment_id")
        public String paymentId;

        @JsonProperty("transaction_id")
        public String transactionId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApplicationContext {
        @JsonProperty("brand_name")
        public String brandName;

        public String locale;

        @JsonProperty("shipping_preference")
        public String shippingPreference;

        @JsonProperty("user_action")
        public String userAction;

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