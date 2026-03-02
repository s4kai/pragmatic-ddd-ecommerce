package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProductById {
    private final ProductRepository productRepository;

    public ProductResponse handle(UUID productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        return ProductResponse.fromDomain(product);
    }
}
