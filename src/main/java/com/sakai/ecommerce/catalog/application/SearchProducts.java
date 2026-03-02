package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProducts {
    private final ProductRepository productRepository;

    public Page<ProductResponse> handle(String name, String description, Pageable pageable) {
        return productRepository.findByFilters(name, description, pageable)
                .map(ProductResponse::fromDomain);
    }
}
