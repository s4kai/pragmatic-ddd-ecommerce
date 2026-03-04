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
class ProductPricingServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductPricingServiceImpl service;

    @Test
    void shouldGetPrice() {
        var productId = UUID.randomUUID();
        var sku = new SKU("SKU-001");
        var variant = mock(ProductVariant.class);
        var product = mock(Product.class);
        var price = Money.of(100);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.findVariantBySKU(sku)).thenReturn(variant);
        when(variant.getPrice()).thenReturn(price);

        var result = service.getPrice(productId, "SKU-001");

        assertEquals(price, result);
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        var productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getPrice(productId, "SKU-001"));
    }
}
