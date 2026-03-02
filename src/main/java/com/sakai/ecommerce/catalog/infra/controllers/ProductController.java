package com.sakai.ecommerce.catalog.infra.controllers;

import com.sakai.ecommerce.catalog.application.*;
import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.infra.requests.CreateProductRequest;
import com.sakai.ecommerce.catalog.infra.requests.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final CreateProduct createProduct;
    private final UpdateProduct updateProduct;
    private final SearchProducts searchProducts;
    private final GetProductById getProductById;

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

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            Pageable pageable
    ) {
        return ResponseEntity.ok(searchProducts.handle(name, description, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID productId) {
        return ResponseEntity.ok(getProductById.handle(productId));
    }
}
