package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;

import java.util.List;

public record CreateProductWithVariantsCommand(
        String name,
        String description,
        ProductDimensionsDTO dimensions,
        List<CreateVariantCommand> variants
) {}
