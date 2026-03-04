package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record DecreaseItemQuantityCommand(
        UUID customerId,
        String sessionId,
        String sku,
        int quantity
) {}
