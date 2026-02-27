package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.UpdateProductWithVariantsCommand;

import java.util.List;
import java.util.UUID;

public record UpdateProductRequest(
        String name,
        String description,
        ProductDimensionsRequest dimensions,
        List<UpdateVariantRequest> variants
) {
    public UpdateProductWithVariantsCommand toCommand(UUID productId) {
        return new UpdateProductWithVariantsCommand(
                productId,
                name,
                description,
                dimensions != null ? dimensions.toDTO() : null,
                variants.stream().map(UpdateVariantRequest::toCommand).toList()
        );
    }
}
