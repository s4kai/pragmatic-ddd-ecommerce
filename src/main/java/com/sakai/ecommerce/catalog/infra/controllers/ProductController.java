package com.sakai.ecommerce.catalog.infra.controllers;

import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.application.handlers.command.CreateProductHandler;
import com.sakai.ecommerce.catalog.application.handlers.command.UpdateProductHandler;
import com.sakai.ecommerce.catalog.application.handlers.query.GetProductByIdHandler;
import com.sakai.ecommerce.catalog.application.handlers.query.SearchProductsHandler;
import com.sakai.ecommerce.catalog.application.queries.GetProductByIdQuery;
import com.sakai.ecommerce.catalog.application.queries.SearchProductsQuery;
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
    private final CreateProductHandler createProductHandler;
    private final UpdateProductHandler updateProductHandler;
    private final SearchProductsHandler searchProductsHandler;
    private final GetProductByIdHandler getProductByIdHandler;

    @PostMapping
    public ResponseEntity<UUID> create(@ModelAttribute CreateProductRequest request) {
        return ResponseEntity.ok(
            createProductHandler.handle(request.toCommand())
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> update(
            @PathVariable UUID productId,
            @ModelAttribute UpdateProductRequest request
    ) {
        updateProductHandler.handle(request.toCommand(productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            Pageable pageable
    ) {
        var query = new SearchProductsQuery(name, description, pageable);
        return ResponseEntity.ok(searchProductsHandler.handle(query));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID productId) {
        var query = new GetProductByIdQuery(productId);
        return ResponseEntity.ok(getProductByIdHandler.handle(query));
    }
}
