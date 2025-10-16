package com.example.dto.subscription;

import com.example.dto.Name;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionCaptureRequest {

    @JsonProperty("subscription_id")
    public String subscriptionId;

    @JsonProperty("return_url")
    public String returnUrl;

    @JsonProperty("cancel_url")
    public String cancelUrl;

    @JsonProperty("custom_id")
    public String customId;

    public String quantity;

    @JsonProperty("shipping_amount")
    public com.example.dto.Money shippingAmount;

    public Subscriber subscriber;

    @JsonProperty("application_context")
    public ApplicationContext applicationContext;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Subscriber {
        public Name name;

        @JsonProperty("email_address")
        public String emailAddress;

        @JsonProperty("shipping_address")
        public ShippingAddress shippingAddress;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ShippingAddress {
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