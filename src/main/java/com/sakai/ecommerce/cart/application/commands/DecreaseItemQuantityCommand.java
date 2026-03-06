package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record DecreaseItemQuantityCommand(
        String sku,
        int quantity
) {}
