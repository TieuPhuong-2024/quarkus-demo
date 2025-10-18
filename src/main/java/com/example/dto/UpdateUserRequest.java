package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private String id;
    private String name;
    private RoleType role;

    public enum RoleType {
        ADMIN, USER, VIP_USER
    }
}
