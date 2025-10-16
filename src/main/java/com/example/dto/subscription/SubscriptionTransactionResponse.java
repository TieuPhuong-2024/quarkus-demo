package com.example.dto.subscription;

import com.example.dto.Money;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionTransactionResponse {

    @JsonProperty("transaction_id")
    public String transactionId;

    public String status;

    @JsonProperty("transaction_type")
    public String transactionType;

    public Money amount;

    @JsonProperty("fee_amount")
    public Money feeAmount;

    @JsonProperty("net_amount")
    public Money netAmount;

    @JsonProperty("currency_code")
    public String currencyCode;

    @JsonProperty("transaction_date")
    public String transactionDate;

    @JsonProperty("payment_instrument_type")
    public String paymentInstrumentType;

    @JsonProperty("payment_instrument_subtype")
    public String paymentInstrumentSubtype;

    @JsonProperty("transaction_subject")
    public String transactionSubject;

    @JsonProperty("custom_field")
    public String customField;

    public List<Link> links;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Link {
        public String href;
        public String rel;
        public String method;
    }
}