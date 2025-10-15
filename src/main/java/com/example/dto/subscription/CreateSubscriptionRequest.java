package com.example.dto.subscription;

import com.example.dto.Money;
import com.example.dto.Subscriber;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateSubscriptionRequest {
    @JsonProperty("plan_id")
    public String planId;

    @JsonProperty("start_time")
    public String startTime;                 // ISO 8601, optional

    public String quantity;                  // optional (mặc định "1")

    @JsonProperty("shipping_amount")
    public Money shippingAmount;             // optional

    public Subscriber subscriber;            // optional

    @JsonProperty("application_context")
    public ApplicationContext applicationContext; // return_url, cancel_url, ...

    @JsonProperty("custom_id")
    public String customId;                  // optional

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ShippingAddress {
        public ShippingName name;
        public Address address;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ShippingName {
        @JsonProperty("full_name")
        public String fullName;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Address {
        @JsonProperty("address_line_1")
        public String addressLine1;
        @JsonProperty("address_line_2")
        public String addressLine2;
        @JsonProperty("admin_area_2")
        public String adminArea2;            // city
        @JsonProperty("admin_area_1")
        public String adminArea1;            // state
        @JsonProperty("postal_code")
        public String postalCode;
        @JsonProperty("country_code")
        public String countryCode;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApplicationContext {
        @JsonProperty("brand_name")
        public String brandName;             // optional
        public String locale;                // optional
        @JsonProperty("shipping_preference")
        public String shippingPreference;    // GET_FROM_FILE | NO_SHIPPING | SET_PROVIDED_ADDRESS
        @JsonProperty("user_action")
        public String userAction;            // SUBSCRIBE_NOW | CONTINUE
        @JsonProperty("payment_method")
        public PaymentMethod paymentMethod;  // optional
        @JsonProperty("return_url")
        public String returnUrl;
        @JsonProperty("cancel_url")
        public String cancelUrl;
        @JsonProperty("landing_page")
        public String landingPage;           // LOGIN | BILLING | NO_PREFERENCE (optional)
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentMethod {
        @JsonProperty("payer_selected")
        public String payerSelected;         // e.g. PAYPAL
        @JsonProperty("payee_preferred")
        public String payeePreferred;        // IMMEDIATE_PAYMENT_REQUIRED | UNRESTRICTED
    }
}
