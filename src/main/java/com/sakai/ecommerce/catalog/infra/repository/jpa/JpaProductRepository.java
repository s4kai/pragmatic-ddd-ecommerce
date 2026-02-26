package com.sakai.ecommerce.catalog.infra.repository.jpa;

import com.sakai.ecommerce.catalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
}
