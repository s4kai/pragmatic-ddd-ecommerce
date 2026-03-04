package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record RemoveItemFromCartCommand(
        UUID customerId,
        String sessionId,
        String sku
) {}
