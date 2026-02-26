package com.sakai.ecommerce.catalog.domain;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    UUID save(Product product);
    Optional<Product> findById(UUID id);
}
