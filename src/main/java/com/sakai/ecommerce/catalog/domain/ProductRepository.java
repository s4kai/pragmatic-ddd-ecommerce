package com.sakai.ecommerce.catalog.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    UUID save(Product product);
    Optional<Product> findById(UUID id);
    Page<Product> findByFilters(String name, String description, Pageable pageable);
}
