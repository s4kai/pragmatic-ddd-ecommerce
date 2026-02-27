package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.commands.UpdateProductWithVariantsCommand;
import com.sakai.ecommerce.catalog.application.commands.UpdateVariantCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.application.handlers.ProductVariantFilesCleanupHandler;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.shared.application.FileUpload;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private StorageService storageService;

    @Mock
    private ProductCleanupService cleanupService;

    @Mock
    private ProductVariantFilesCleanupHandler filesCleanupHandler;

    @InjectMocks
    private UpdateProduct updateProduct;

    @Test
    void shouldUpdateProductSuccessfully() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(storageService.store(any())).thenReturn("new/path.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of("new/gallery.jpg"));

        assertDoesNotThrow(() -> updateProduct.handle(command));

        verify(productRepository).save(product);
        verify(eventPublisher).publish(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var productId = UUID.randomUUID();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(BusinessError.class, () -> updateProduct.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateSKUs() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = new UpdateProductWithVariantsCommand(
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

        assertThrows(BusinessError.class, () -> updateProduct.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProductDimensions() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var dimensions = new ProductDimensionsDTO(15.0, 25.0, 35.0, 7.5);
        var command = new UpdateProductWithVariantsCommand(
            productId,
            "Updated Name",
            "Updated Description",
            dimensions,
            List.of(validVariantCommand("SKU-001"))
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(storageService.store(any())).thenReturn("path.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of());

        assertDoesNotThrow(() -> updateProduct.handle(command));
        verify(productRepository).save(product);
    }

    private Product createProduct() {
        var variant = new ProductVariant(
            new SKU("SKU-001"),
            "Variant Name",
            new Money(BigDecimal.valueOf(100), "BRL")
        );
        variant.updateCoverImage("old/cover.jpg");
        variant.updateGallery(List.of("old/gallery1.jpg"));

        return new Product("Product Name", "Description", List.of(variant));
    }

    private UpdateProductWithVariantsCommand validCommand(UUID productId) {
        return new UpdateProductWithVariantsCommand(
            productId,
            "Updated Product Name",
            "Updated Description",
            null,
            List.of(validVariantCommand("SKU-001"))
        );
    }

    private UpdateVariantCommand validVariantCommand(String sku) {
        var coverImage = new FileUpload(
            "cover.jpg",
            () -> new ByteArrayInputStream(new byte[0]),
            "image/jpeg",
            1024L
        );

        return new UpdateVariantCommand(
            UUID.randomUUID(),
            sku,
            "Updated Variant Name",
            coverImage,
            BigDecimal.valueOf(150),
            "BRL",
            List.of(),
            Map.of("color", "blue")
        );
    }
}
