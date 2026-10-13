package com.example.sonarqubetest.auth;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        LocalDateTime createdAt
) implements Serializable {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getCreatedAt());
    }
}
