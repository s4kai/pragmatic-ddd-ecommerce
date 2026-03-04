package com.sakai.ecommerce.catalog.application.commands;

import com.sakai.ecommerce.shared.application.FileUpload;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record CreateVariantCommand(
        String sku,
        String name,
        FileUpload coverImage,
        BigDecimal price,
        String currency,
        List<FileUpload> gallery,
        Map<String, Object> details
) {}
