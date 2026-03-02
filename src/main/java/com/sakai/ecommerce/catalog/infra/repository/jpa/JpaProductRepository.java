package com.sakai.ecommerce.catalog.infra.repository.jpa;

import com.sakai.ecommerce.catalog.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productVariants WHERE " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<Product> findByFilters(@Param("name") String name, 
                                @Param("description") String description, 
                                Pageable pageable);
}
