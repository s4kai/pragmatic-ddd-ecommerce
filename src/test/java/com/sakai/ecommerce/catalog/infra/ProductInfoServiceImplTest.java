package com.sakai.ecommerce.catalog.infra;

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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductInfoServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductInfoServiceImpl service;

    @Test
    void shouldGetProductInfo() {
        var productId = UUID.randomUUID();
        var sku = new SKU("SKU-001");
        var variant = mock(ProductVariant.class);
        var product = mock(Product.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.findVariantBySKU(sku)).thenReturn(variant);
        when(product.getName()).thenReturn("Product Name");
        when(variant.getName()).thenReturn("Variant Name");
        when(variant.getCoverImage()).thenReturn("image.jpg");

        var result = service.getProductInfo(productId, "SKU-001");

        assertTrue(result.isPresent());
        assertEquals("Product Name", result.get().productName());
        assertEquals("Variant Name", result.get().variantName());
        assertEquals("image.jpg", result.get().coverImage());
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        var productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        var result = service.getProductInfo(productId, "SKU-001");

        assertTrue(result.isEmpty());
    }
}
