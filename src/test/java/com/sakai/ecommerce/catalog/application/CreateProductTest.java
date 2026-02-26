package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.commands.CreateProductWithVariantsCommand;
import com.sakai.ecommerce.catalog.application.commands.CreateVariantCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.Product;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.shared.application.FileUpload;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductTest {

    @Mock
    private VariantMapper variantMapper;

    @Mock
    private ProductCleanupService cleanupService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private CreateProduct createProduct;

    @Test
    void shouldCreateProductSuccessfully() {
        var command = validCommand();
        var productId = UUID.randomUUID();

        when(storageService.store(any())).thenReturn("path/to/image.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of("path1.jpg", "path2.jpg"));
        when(productRepository.save(any(Product.class))).thenReturn(productId);

        var result = createProduct.handle(command);

        assertEquals(productId, result);
        verify(productRepository).save(any(Product.class));
        verify(eventPublisher).publish(any(Product.class));
        verify(cleanupService, never()).cleanupVariantFiles(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateSKUs() {
        var command = new CreateProductWithVariantsCommand(
            "Product Name",
            "Description",
            null,
            List.of(
                validVariantCommand("SKU-001"),
                validVariantCommand("SKU-001")
            )
        );

        assertThrows(BusinessError.class, () -> createProduct.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldCleanupFilesWhenExceptionOccurs() {
        var command = validCommand();

        when(storageService.store(any())).thenReturn("path/to/image.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of("path1.jpg"));
        when(variantMapper.map(any(), any(), any())).thenReturn(mock(ProductVariant.class));
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> createProduct.handle(command));
        verify(cleanupService).cleanupVariantFiles(any());
    }

    @Test
    void shouldCreateProductWithDimensions() {
        var dimensions = new ProductDimensionsDTO(10.0, 20.0, 30.0, 5.0);
        var command = new CreateProductWithVariantsCommand(
            "Product Name",
            "Description",
            dimensions,
            List.of(validVariantCommand("SKU-001"))
        );
        var productId = UUID.randomUUID();

        when(storageService.store(any())).thenReturn("path/to/image.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of());
        when(variantMapper.map(any(), any(), any())).thenReturn(mock(ProductVariant.class));
        when(productRepository.save(any(Product.class))).thenReturn(productId);

        var result = createProduct.handle(command);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    private CreateProductWithVariantsCommand validCommand() {
        return new CreateProductWithVariantsCommand(
            "Product Name",
            "Product Description",
            null,
            List.of(validVariantCommand("SKU-001"))
        );
    }

    private CreateVariantCommand validVariantCommand(String sku) {
        Supplier<InputStream> imageData = () -> new ByteArrayInputStream(new byte[0]);

        var coverImage = new FileUpload(
            "cover.jpg",
            imageData,
            "image/jpeg",
            1024L
        );

        var gallery = List.of(
            new FileUpload("gallery1.jpg", imageData, "image/jpeg", 1024L),
            new FileUpload("gallery2.jpg", imageData, "image/jpeg", 1024L)
        );

        Map<String, Object> details = Map.of("color", "red", "size", "M");
        
        return new CreateVariantCommand(
                sku,
                "Variant Name",
                coverImage,
                BigDecimal.valueOf(100),
                "BRL",
                gallery,
                details
        );
    }
}
