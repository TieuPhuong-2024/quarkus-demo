package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Frequency {
    @JsonProperty("interval_unit")
    public String intervalUnit; // DAY | WEEK | MONTH | YEAR
    @JsonProperty("interval_count")
    public Integer intervalCount; // e.g. 1
}
