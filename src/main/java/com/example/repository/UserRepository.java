package com.example.repository;

import com.example.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public List<User> findByActive(boolean active) {
        return find("isActive", active).list();
    }

    public boolean existsByEmail(String email) {
        return find("email", email).count() > 0;
    }
}