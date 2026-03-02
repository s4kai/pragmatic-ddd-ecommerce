package com.sakai.ecommerce.catalog.application.dto;

import com.sakai.ecommerce.catalog.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private ProductDimensionsDTO dimensions;
    private List<ProductVariantResponse> variants;

    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getDimensions() != null ? ProductDimensionsDTO.fromDomain(product.getDimensions()) : null,
                product.getProductVariants().stream()
                        .map(ProductVariantResponse::fromDomain)
                        .toList()
        );
    }
}
