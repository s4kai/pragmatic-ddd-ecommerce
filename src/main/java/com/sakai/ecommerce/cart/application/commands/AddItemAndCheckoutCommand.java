package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record AddItemAndCheckoutCommand(
        UUID productId,
        String sku,
        int quantity
) {}
