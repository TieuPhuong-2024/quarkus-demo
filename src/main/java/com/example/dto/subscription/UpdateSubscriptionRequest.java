package com.example.dto.subscription;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateSubscriptionRequest {

    @JsonProperty("shipping_address")
    public ShippingAddress shippingAddress;

    @JsonProperty("shipping_amount")
    public com.example.dto.Money shippingAmount;

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
}