package com.sakai.ecommerce.cart.application.commands;

import java.math.BigDecimal;
import java.util.UUID;

public record AddItemToCartCommand(
        UUID customerId,
        String sessionId,
        UUID productId,
        String sku,
        int quantity
) {}
