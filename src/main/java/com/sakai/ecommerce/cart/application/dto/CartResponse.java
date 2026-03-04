package com.sakai.ecommerce.cart.application.dto;

import com.sakai.ecommerce.shared.domain.Money;

import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID id,
        UUID customerId,
        String sessionId,
        String status,
        List<CartItemResponse> items,
        Money total
) {
    public record CartItemResponse(
            UUID productId,
            String sku,
            int quantity,
            Money unitPrice,
            Money subtotal,
            ProductInfo productInfo
    ) {}

    public record ProductInfo(
            String productName,
            String variantName,
            String coverImage
    ) {}
}
