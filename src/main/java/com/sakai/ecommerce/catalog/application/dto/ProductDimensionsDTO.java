package com.sakai.ecommerce.catalog.application.dto;

import com.sakai.ecommerce.catalog.domain.ProductDimensions;

public record ProductDimensionsDTO(
        double height,
        double width,
        double depth,
        double weight
) {
    public static ProductDimensionsDTO fromDomain(ProductDimensions dimensions) {
        return new ProductDimensionsDTO(
                dimensions.getHeight(),
                dimensions.getWidth(),
                dimensions.getDepth(),
                dimensions.getWeight()
        );
    }
}
