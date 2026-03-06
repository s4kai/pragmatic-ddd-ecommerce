package com.sakai.ecommerce.auth.application.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    Long expiresIn
) {}
