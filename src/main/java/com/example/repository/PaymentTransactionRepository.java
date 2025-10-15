package com.example.repository;

import com.example.entity.PaymentTransaction;
import com.example.entity.PaymentStatus;
import com.example.entity.Subscription;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PaymentTransactionRepository implements PanacheRepository<PaymentTransaction> {

    public List<PaymentTransaction> findBySubscription(Subscription subscription) {
        return find("subscription", subscription).list();
    }

    public List<PaymentTransaction> findBySubscriptionId(Long subscriptionId) {
        return find("subscription.id", subscriptionId).list();
    }

    public List<PaymentTransaction> findByStatus(PaymentStatus status) {
        return find("status", status).list();
    }

    public List<PaymentTransaction> findByPaypalTransactionId(String paypalTransactionId) {
        return find("paypalTransactionId", paypalTransactionId).list();
    }

    public List<PaymentTransaction> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return find("paymentDate between ?1 and ?2", startDate, endDate).list();
    }

    public List<PaymentTransaction> findBySubscriptionAndStatus(Subscription subscription, PaymentStatus status) {
        return find("subscription = ?1 and status = ?2", subscription, status).list();
    }
}