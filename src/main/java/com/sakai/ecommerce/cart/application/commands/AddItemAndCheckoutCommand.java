package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record AddItemAndCheckoutCommand(
        UUID customerId,
        String sessionId,
        UUID productId,
        String sku,
        int quantity
) {}
