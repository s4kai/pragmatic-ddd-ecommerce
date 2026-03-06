package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;

import java.util.List;

public record CreateProductCommand(
        String name,
        String description,
        ProductDimensionsDTO dimensions,
        List<CreateVariantDataCommand> variants
) {}
