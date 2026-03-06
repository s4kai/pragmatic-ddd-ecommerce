package com.sakai.ecommerce.auth.application.commands;

public record RefreshTokenCommand(
    String refreshToken
) {}
