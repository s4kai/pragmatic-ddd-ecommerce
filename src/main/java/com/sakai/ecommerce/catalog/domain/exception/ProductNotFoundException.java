package com.sakai.ecommerce.catalog.domain.exception;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

import java.util.UUID;

public class ProductNotFoundException extends BusinessError {
    public ProductNotFoundException(UUID productId) {
        super("Product not found: " + productId);
    }
}
