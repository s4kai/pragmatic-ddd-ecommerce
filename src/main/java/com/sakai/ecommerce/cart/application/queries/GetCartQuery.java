package com.sakai.ecommerce.cart.application.queries;

import java.util.UUID;

public record GetCartQuery(
        UUID userId,
        String sessionId
) {}
