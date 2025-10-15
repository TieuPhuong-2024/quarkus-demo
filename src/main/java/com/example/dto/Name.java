package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Name {
    @JsonProperty("given_name")
    public String givenName;
    public String surname;
}
