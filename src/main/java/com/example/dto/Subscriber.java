package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subscriber {
    public Name name;
    @JsonProperty("email_address")
    public String emailAddress;
    @JsonProperty("payer_id")
    public String payerId;
}
