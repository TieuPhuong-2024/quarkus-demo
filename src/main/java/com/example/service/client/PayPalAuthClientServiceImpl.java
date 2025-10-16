package com.example.service.client;

import com.example.client.PayPalAuthClient;
import com.example.config.PayPalConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@ApplicationScoped
public class PayPalAuthClientServiceImpl implements PayPalAuthClientService {
    @Inject
    PayPalConfig cfg;

    @Inject
    @RestClient
    PayPalAuthClient authClient;

    @Override
    public String getAccessToken() {
        String basic = Base64.getEncoder().encodeToString(
                (cfg.clientId() + ":" + cfg.clientSecret()).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> resp = authClient.token("Basic " + basic, "client_credentials");
        String accessToken = (String) resp.get("access_token");
        return "Bearer " + accessToken;
    }
}
