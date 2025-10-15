package com.example.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "paypal")
public interface PayPalConfig {
    String baseUrl();
    String clientId();
    String clientSecret();
}
