package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.commands.CreateProductCommand;
import com.sakai.ecommerce.catalog.application.commands.CreateVariantDataCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.exception.DuplicateSKUException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CreateProductHandler createProductHandler;

    @Test
    void shouldCreateProductSuccessfully() {
        var command = validCommand();
        var productId = UUID.randomUUID();

        when(productRepository.save(any())).thenReturn(productId);

        var result = createProductHandler.handle(command);

        assertEquals(productId, result);
        verify(productRepository).save(any());
        verify(eventPublisher).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateSKUs() {
        var command = new CreateProductCommand(
            "Product Name",
            "Description",
            null,
            List.of(
                validVariantCommand("SKU-001"),
                validVariantCommand("SKU-001")
            )
        );

        assertThrows(DuplicateSKUException.class, () -> createProductHandler.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSaveFails() {
        var command = validCommand();

        when(productRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> createProductHandler.handle(command));
    }

    @Test
    void shouldCreateProductWithDimensions() {
        var dimensions = new ProductDimensionsDTO(10.0, 20.0, 30.0, 5.0);
        var command = new CreateProductCommand(
            "Product Name",
            "Description",
            dimensions,
            List.of(validVariantCommand("SKU-001"))
        );
        var productId = UUID.randomUUID();

        when(productRepository.save(any())).thenReturn(productId);

        var result = createProductHandler.handle(command);

        assertNotNull(result);
        verify(productRepository).save(any());
    }

    private CreateProductCommand validCommand() {
        return new CreateProductCommand(
            "Product Name",
            "Product Description",
            null,
            List.of(validVariantCommand("SKU-001"))
        );
    }

    private CreateVariantDataCommand validVariantCommand(String sku) {
        return new CreateVariantDataCommand(
            sku,
            "Variant Name",
            BigDecimal.valueOf(100),
            "BRL",
            Map.of("color", "red", "size", "M")
        );
    }
}
