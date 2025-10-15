package com.example.dto.webhook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatchOperation {
    private String op;
    private String path;
    private Object value;
    @JsonProperty("from")
    private String from;
}