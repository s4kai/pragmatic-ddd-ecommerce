package com.sakai.ecommerce.auth.application.services;

import com.sakai.ecommerce.auth.domain.Role;

import java.util.List;
import java.util.UUID;

public interface TokenService {
    String generateAccessToken(UUID userId, String email, List<Role> roles);
    String generateRefreshToken(UUID userId);
    UUID validateAccessToken(String accessToken);
    UUID validateRefreshToken(String refreshToken);
    List<String> extractRoles(String accessToken);
    Long getAccessTokenExpiresIn();
}
