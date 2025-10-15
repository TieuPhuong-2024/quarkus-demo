package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Taxes {
    public String percentage; // "10" for 10%
    public Boolean inclusive; // true/false
}
