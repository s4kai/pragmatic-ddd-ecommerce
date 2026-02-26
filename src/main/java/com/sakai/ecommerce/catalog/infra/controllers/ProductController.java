package com.sakai.ecommerce.catalog.infra.controllers;

import com.sakai.ecommerce.catalog.application.CreateProduct;
import com.sakai.ecommerce.catalog.infra.requests.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final CreateProduct createProduct;

    @PostMapping
    public ResponseEntity<UUID> create(@ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(
            createProduct.handle(request.toCommand())
        );
    }
}
