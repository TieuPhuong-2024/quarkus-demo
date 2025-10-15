package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Money {
    @JsonProperty("currency_code")
    public String currencyCode;
    public String value;
}
