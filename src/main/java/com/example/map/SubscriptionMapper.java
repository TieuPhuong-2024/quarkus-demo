package com.example.map;

import com.example.dto.subscription.CreatePayPalSubscriptionResponse;
import com.example.entity.Subscription;
import com.example.entity.SubscriptionStatus;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubscriptionMapper {

    public Subscription toEntity(
            CreatePayPalSubscriptionResponse response) {
        if (response == null) {
            return null;
        }
        Subscription.SubscriptionBuilder builder = Subscription.builder();

        var approveLink = response.getLinks().stream()
                .filter(link -> "approve".equals(link.getRel()))
                .findFirst()
                .map(CreatePayPalSubscriptionResponse.Link::getHref)
                .orElse("");

        builder
                .id(response.getId())
                .status(mapStatus(response.getStatus()))
                .createTime(response.getCreateTime())
                .approveLink(approveLink);

        return builder.build();
    }

    private static SubscriptionStatus mapStatus(String status) {
        if (status == null)
            return null;
        try {
            return SubscriptionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
