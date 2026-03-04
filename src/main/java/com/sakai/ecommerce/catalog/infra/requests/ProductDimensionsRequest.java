package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;

public record ProductDimensionsRequest(
        double height,
        double width,
        double depth,
        double weight
) {
    public ProductDimensionsDTO toDTO() {
        return new ProductDimensionsDTO(height, width, depth, weight);
    }
}