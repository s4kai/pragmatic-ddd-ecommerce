package com.sakai.ecommerce.auth.application.dto;

import java.util.UUID;

public record UserResponse(
    UUID id,
    String email
) {}
