package com.sakai.ecommerce.cart.infra.requests;

import java.util.UUID;

public record AddItemRequest(
    UUID productId,
    String sku,
    int quantity
) {}
