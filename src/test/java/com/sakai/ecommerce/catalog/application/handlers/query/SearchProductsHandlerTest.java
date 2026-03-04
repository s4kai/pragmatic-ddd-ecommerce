package com.sakai.ecommerce.catalog.application.handlers.query;

import com.sakai.ecommerce.catalog.application.queries.SearchProductsQuery;
import com.sakai.ecommerce.catalog.domain.Product;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchProductsHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SearchProductsHandler handler;

    @Test
    void shouldReturnPageOfProducts() {
        var pageable = PageRequest.of(0, 10);
        var products = List.of(createProduct());
        var page = new PageImpl<>(products, pageable, products.size());
        var query = new SearchProductsQuery("Product", "Description", pageable);

        when(productRepository.findByFilters("Product", "Description", pageable)).thenReturn(page);

        var response = handler.handle(query);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Product Name", response.getContent().get(0).getName());
        verify(productRepository).findByFilters("Product", "Description", pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoProductsFound() {
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<Product>(List.of(), pageable, 0);
        var query = new SearchProductsQuery("NonExistent", null, pageable);

        when(productRepository.findByFilters("NonExistent", null, pageable)).thenReturn(page);

        var response = handler.handle(query);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());
        verify(productRepository).findByFilters("NonExistent", null, pageable);
    }

    private Product createProduct() {
        return new Product(
                "Product Name",
                "Product Description",
                List.of(new ProductVariant(
                        new SKU("SKU-001"),
                        "Variant",
                        new Money(BigDecimal.valueOf(100), "BRL")
                ))
        );
    }
}
