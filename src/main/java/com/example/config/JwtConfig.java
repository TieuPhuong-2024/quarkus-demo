package com.example.config;


import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "jwt")
public interface JwtConfig {
    String key();
}
