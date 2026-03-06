package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;

import java.util.List;
import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        String name,
        String description,
        ProductDimensionsDTO dimensions,
        List<UpdateVariantDataCommand> variants
) {}
