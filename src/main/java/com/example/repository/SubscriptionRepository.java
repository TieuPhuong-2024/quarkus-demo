package com.example.repository;

import com.example.entity.Subscription;
import com.example.entity.SubscriptionStatus;
import com.example.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SubscriptionRepository implements PanacheRepository<Subscription> {

    public List<Subscription> findByUser(User user) {
        return find("user", user).list();
    }

    public List<Subscription> findByUserId(Long userId) {
        return find("user.id", userId).list();
    }

    public List<Subscription> findByStatus(SubscriptionStatus status) {
        return find("status", status).list();
    }

    public List<Subscription> findByUserAndStatus(User user, SubscriptionStatus status) {
        return find("user = ?1 and status = ?2", user, status).list();
    }

    public List<Subscription> findByPaypalSubscriptionId(String paypalSubscriptionId) {
        return find("paypalSubscriptionId", paypalSubscriptionId).list();
    }

    public Subscription findByPaypalSubscriptionIdAndUser(String paypalSubscriptionId, User user) {
        return find("paypalSubscriptionId = ?1 and user = ?2", paypalSubscriptionId, user).firstResult();
    }

    public List<Subscription> findByNextBillingDateBefore(LocalDateTime date) {
        return find("nextBillingDate < ?1", date).list();
    }

    public List<Subscription> findByStatusIn(List<SubscriptionStatus> statuses) {
        return find("status in ?1", statuses).list();
    }

    public boolean existsByPaypalSubscriptionId(String paypalSubscriptionId) {
        return find("paypalSubscriptionId", paypalSubscriptionId).count() > 0;
    }

    public long countByUserAndStatus(User user, SubscriptionStatus status) {
        return find("user = ?1 and status = ?2", user, status).count();
    }

    public List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status) {
        return find("user.id = ?1 and status = ?2", userId, status).list();
    }
}