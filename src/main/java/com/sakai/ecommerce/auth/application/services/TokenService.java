package com.sakai.ecommerce.auth.application.services;

import java.util.UUID;

public interface TokenService {
    String generateAccessToken(UUID userId, String email);
    String generateRefreshToken(UUID userId);
    UUID validateAccessToken(String accessToken);
    UUID validateRefreshToken(String refreshToken);
    Long getAccessTokenExpiresIn();
}
