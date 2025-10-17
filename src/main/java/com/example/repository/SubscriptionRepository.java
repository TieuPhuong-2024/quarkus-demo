package com.example.repository;

import com.example.entity.Subscription;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SubscriptionRepository implements PanacheRepository<Subscription> {

    public Optional<Subscription> findBySubscriptionId(String subscriptionId) {
        return find("id", subscriptionId).firstResultOptional();
    }

    public Optional<Subscription> findByUserId(String userId) {
        return find("userId", userId).firstResultOptional();
    }
}