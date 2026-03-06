package com.sakai.ecommerce.catalog.infra.requests;

import com.sakai.ecommerce.catalog.application.commands.UpdateProductCommand;

import java.util.List;
import java.util.UUID;

public record UpdateProductDataRequest(
        String name,
        String description,
        ProductDimensionsRequest dimensions,
        List<UpdateVariantDataRequest> variants
) {
    public UpdateProductCommand toCommand(UUID productId) {
        return new UpdateProductCommand(
                productId,
                name,
                description,
                dimensions != null ? dimensions.toDTO() : null,
                variants.stream().map(UpdateVariantDataRequest::toCommand).toList()
        );
    }
}
