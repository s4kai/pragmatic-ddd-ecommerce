package com.sakai.ecommerce.catalog;

import com.sakai.ecommerce.shared.domain.Money;

import java.util.UUID;

public interface ProductPricingService {
    Money getPrice(UUID productId, String sku);
}
