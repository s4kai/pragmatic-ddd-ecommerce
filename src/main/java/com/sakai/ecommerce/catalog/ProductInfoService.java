package com.sakai.ecommerce.catalog;

import java.util.Optional;
import java.util.UUID;

public interface ProductInfoService {
    Optional<ProductInfo> getProductInfo(UUID productId, String sku);

    record ProductInfo(
            String productName,
            String variantName,
            String coverImage
    ) {}
}
