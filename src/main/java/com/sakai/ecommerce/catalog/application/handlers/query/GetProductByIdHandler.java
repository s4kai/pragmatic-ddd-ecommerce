package com.sakai.ecommerce.catalog.application.handlers.query;

import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.application.queries.GetProductByIdQuery;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductByIdHandler {
    private final ProductRepository productRepository;

    public ProductResponse handle(GetProductByIdQuery query) {
        var product = productRepository.findById(query.productId())
                .orElseThrow(() -> new ProductNotFoundException(query.productId()));
        
        return ProductResponse.fromDomain(product);
    }
}
