package com.example.service.client;

import com.example.dto.UpdateUserRequest;

public interface UserClientService {
    void update(String bearerToken, UpdateUserRequest request);
}
