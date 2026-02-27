package com.sakai.ecommerce.catalog.infra.controllers;

import com.sakai.ecommerce.catalog.application.CreateProduct;
import com.sakai.ecommerce.catalog.application.UpdateProduct;
import com.sakai.ecommerce.catalog.infra.requests.CreateProductRequest;
import com.sakai.ecommerce.catalog.infra.requests.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final CreateProduct createProduct;
    private final UpdateProduct updateProduct;

    @PostMapping
    public ResponseEntity<UUID> create(@ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(
            createProduct.handle(request.toCommand())
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> update(
            @PathVariable UUID productId,
            @ModelAttribute UpdateProductRequest request
    ) {
        updateProduct.handle(request.toCommand(productId));
        return ResponseEntity.noContent().build();
    }
}
