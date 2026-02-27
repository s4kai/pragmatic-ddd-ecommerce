package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;

import java.util.List;
import java.util.UUID;

public record UpdateProductWithVariantsCommand(
        UUID productId,
        String name,
        String description,
        ProductDimensionsDTO dimensions,
        List<UpdateVariantCommand> variants
) {
    public UpdateProductWithVariantsCommand {
        if (productId == null) {
            throw new BusinessError("Product ID cannot be null");
        }
        if (variants == null || variants.isEmpty()) {
            throw new BusinessError("Variants cannot be empty");
        }
    }
}
