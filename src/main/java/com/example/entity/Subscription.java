package com.example.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    private String id;

    @Column(name = "paypal_subscription_id", unique = true, nullable = false)
    private String paypalSubscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Column(name = "quantity")
    @Builder.Default
    private String quantity = "1";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "next_billing_date")
    private LocalDateTime nextBillingDate;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "suspended_at")
    private LocalDateTime suspendedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "custom_id")
    private String customId;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "plan_description")
    private String planDescription;

    @Column(name = "billing_amount", precision = 10, scale = 2)
    private BigDecimal billingAmount;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "billing_frequency")
    private String billingFrequency;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    @PrePersist
    public void init() {
        id = "SUB-" + UUID.randomUUID();
    }

    // Helper methods
    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public boolean isPending() {
        return status == SubscriptionStatus.APPROVAL_PENDING || status == SubscriptionStatus.CREATED;
    }

    public boolean isCancelled() {
        return status == SubscriptionStatus.CANCELLED;
    }

    public boolean isSuspended() {
        return status == SubscriptionStatus.SUSPENDED;
    }

    public boolean isExpired() {
        return status == SubscriptionStatus.EXPIRED;
    }
}