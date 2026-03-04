package com.sakai.ecommerce.cart.infra.requests;

public record DecreaseItemQuantityRequest(
    String sku,
    int quantity
) {}
