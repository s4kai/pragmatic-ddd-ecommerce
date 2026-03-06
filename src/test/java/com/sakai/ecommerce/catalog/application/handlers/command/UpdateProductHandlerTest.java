package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.commands.UpdateProductCommand;
import com.sakai.ecommerce.catalog.application.commands.UpdateVariantDataCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UpdateProductHandler updateProductHandler;

    @Test
    void shouldUpdateProductSuccessfully() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> updateProductHandler.handle(command));

        verify(productRepository).save(product);
        verify(eventPublisher).publish(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var productId = UUID.randomUUID();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> updateProductHandler.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateSKUs() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = new UpdateProductCommand(
            productId,
            "Updated Name",
            "Updated Description",
            null,
            List.of(
                validVariantCommand("SKU-001"),
                validVariantCommand("SKU-001")
            )
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(BusinessError.class, () -> updateProductHandler.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProductDimensions() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var dimensions = new ProductDimensionsDTO(15.0, 25.0, 35.0, 7.5);
        var command = new UpdateProductCommand(
            productId,
            "Updated Name",
            "Updated Description",
            dimensions,
            List.of(validVariantCommand("SKU-001"))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> updateProductHandler.handle(command));
        verify(productRepository).save(product);
    }

    private Product createProduct() {
        var variant = new ProductVariant(
            new SKU("SKU-001"),
            "Variant Name",
            new Money(BigDecimal.valueOf(100), "BRL")
        );
        return new Product("Product Name", "Description", List.of(variant));
    }

    private UpdateProductCommand validCommand(UUID productId) {
        return new UpdateProductCommand(
            productId,
            "Updated Product Name",
            "Updated Description",
            null,
            List.of(validVariantCommand("SKU-001"))
        );
    }

    private UpdateVariantDataCommand validVariantCommand(String sku) {
        return new UpdateVariantDataCommand(
            sku,
            "Updated Variant Name",
            BigDecimal.valueOf(150),
            "BRL",
            Map.of("color", "blue")
        );
    }
}
