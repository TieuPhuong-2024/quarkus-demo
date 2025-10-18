package com.example.service.client;

import com.example.client.UserClient;
import com.example.dto.UpdateUserRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserClientServiceImpl implements UserClientService {

    @RestClient
    @Inject
    UserClient client;

    @Override
    public void update(String bearerToken, UpdateUserRequest request) {
        client.update(bearerToken, request);
    }
}
