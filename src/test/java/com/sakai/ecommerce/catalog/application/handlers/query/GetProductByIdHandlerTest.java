package com.sakai.ecommerce.catalog.application.handlers.query;

import com.sakai.ecommerce.catalog.application.queries.GetProductByIdQuery;
import com.sakai.ecommerce.catalog.domain.Product;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductByIdHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private GetProductByIdHandler handler;

    @Test
    void shouldReturnProductWhenFound() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var query = new GetProductByIdQuery(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        var response = handler.handle(query);

        assertNotNull(response);
        assertEquals(product.getName(), response.getName());
        verify(productRepository).findById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var productId = UUID.randomUUID();
        var query = new GetProductByIdQuery(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> handler.handle(query));
        verify(productRepository).findById(productId);
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
