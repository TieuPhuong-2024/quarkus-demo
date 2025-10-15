package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Column(name = "paypal_transaction_id", unique = true)
    private String paypalTransactionId;

    @Column(name = "paypal_payment_id")
    private String paypalPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @Column(name = "failure_reason")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount;

    // Additional PayPal specific fields
    @Column(name = "intent")
    private String intent;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payer_info")
    private String payerInfo; // JSON string with payer details

    @Column(name = "billing_info")
    private String billingInfo; // JSON string with billing details

    @PrePersist
    public void init() {
        id = "PAY-" + UUID.randomUUID();
    }
}