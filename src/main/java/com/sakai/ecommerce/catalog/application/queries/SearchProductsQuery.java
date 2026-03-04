package com.sakai.ecommerce.catalog.application.queries;

import org.springframework.data.domain.Pageable;

public record SearchProductsQuery(
    String name,
    String description,
    Pageable pageable
) {}
