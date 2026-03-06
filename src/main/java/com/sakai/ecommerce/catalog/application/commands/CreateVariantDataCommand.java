package com.sakai.ecommerce.catalog.application.commands;

import java.math.BigDecimal;
import java.util.Map;

public record CreateVariantDataCommand(
        String sku,
        String name,
        BigDecimal price,
        String currency,
        Map<String, Object> details
) {}
