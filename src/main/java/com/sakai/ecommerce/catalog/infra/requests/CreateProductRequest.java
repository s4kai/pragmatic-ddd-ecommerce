package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.CreateProductWithVariantsCommand;

import java.util.List;

public record CreateProductRequest(
        String name,
        String description,
        ProductDimensionsRequest dimensions,
        List<CreateVariantRequest> variants
) {
    public CreateProductWithVariantsCommand toCommand() {
        return new CreateProductWithVariantsCommand(
                name,
                description,
                dimensions != null ? dimensions.toDTO() : null,
                variants.stream().map(CreateVariantRequest::toCommand).toList()
        );
    }
}
