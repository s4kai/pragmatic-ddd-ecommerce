package com.sakai.ecommerce.catalog.application.handlers.query;

import com.sakai.ecommerce.catalog.application.dto.ProductResponse;
import com.sakai.ecommerce.catalog.application.queries.SearchProductsQuery;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductsHandler {
    private final ProductRepository productRepository;

    public Page<ProductResponse> handle(SearchProductsQuery query) {
        return productRepository.findByFilters(query.name(), query.description(), query.pageable())
                .map(ProductResponse::fromDomain);
    }
}
