package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.CreateProductCommand;

import java.util.List;

public record CreateProductDataRequest(
        String name,
        String description,
        ProductDimensionsRequest dimensions,
        List<CreateVariantDataRequest> variants
) {
    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
                name,
                description,
                dimensions != null ? dimensions.toDTO() : null,
                variants.stream().map(CreateVariantDataRequest::toCommand).toList()
        );
    }
}
