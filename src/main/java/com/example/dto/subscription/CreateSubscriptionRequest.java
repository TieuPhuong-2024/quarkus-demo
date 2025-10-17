package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSubscriptionRequest {

    @JsonProperty("plan_id")
    private String planId;

    @JsonProperty("start_time")
    private String startTime;

    @JsonProperty("shipping_amount")
    private ShippingAmount shippingAmount;

    @JsonProperty("subscriber")
    private Subscriber subscriber;

    @JsonProperty("application_context")
    private ApplicationContext applicationContext;

    // Các lớp lồng vào bên trong
    @Data
    @NoArgsConstructor
    public static class ShippingAmount {
        @JsonProperty("currency_code")
        private String currencyCode;
        @JsonProperty("value")
        private String value;
    }

    @Data
    @NoArgsConstructor
    public static class Subscriber {
        @JsonProperty("name")
        private Name name;
        @JsonProperty("email_address")
        private String emailAddress;
        @JsonProperty("shipping_address")
        private ShippingAddress shippingAddress;
    }

    @Data
    @NoArgsConstructor
    public static class Name {
        @JsonProperty("given_name")
        private String givenName;
        @JsonProperty("surname")
        private String surname;
    }

    @Data
    @NoArgsConstructor
    public static class ShippingAddress {
        @JsonProperty("name")
        private FullName name;
        @JsonProperty("address")
        private Address address;
    }

    @Data
    @NoArgsConstructor
    public static class FullName {
        @JsonProperty("full_name")
        private String fullName;
    }

    @Data
    @NoArgsConstructor
    public static class Address {
        @JsonProperty("address_line_1")
        private String addressLine1;
        @JsonProperty("address_line_2")
        private String addressLine2;
        @JsonProperty("admin_area_2")
        private String adminArea2;
        @JsonProperty("admin_area_1")
        private String adminArea1;
        @JsonProperty("postal_code")
        private String postalCode;
        @JsonProperty("country_code")
        private String countryCode;
    }

    @Data
    @NoArgsConstructor
    public static class ApplicationContext {
        @JsonProperty("brand_name")
        private String brandName;
        @JsonProperty("locale")
        private String locale;
        @JsonProperty("shipping_preference")
        private String shippingPreference;
        @JsonProperty("user_action")
        private String userAction;
        @JsonProperty("payment_method")
        private PaymentMethod paymentMethod;
        @JsonProperty("return_url")
        private String returnUrl;
        @JsonProperty("cancel_url")
        private String cancelUrl;
    }

    @Data
    @NoArgsConstructor
    public static class PaymentMethod {
        @JsonProperty("payer_selected")
        private String payerSelected;
        @JsonProperty("payee_preferred")
        private String payeePreferred;
    }
}