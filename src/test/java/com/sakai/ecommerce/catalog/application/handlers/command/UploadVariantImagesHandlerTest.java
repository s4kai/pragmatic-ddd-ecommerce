package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.ProductCleanupService;
import com.sakai.ecommerce.catalog.application.commands.UploadVariantImagesCommand;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.application.FileUpload;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadVariantImagesHandlerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private ProductCleanupService cleanupService;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private UploadVariantImagesHandler handler;

    @Test
    void shouldUploadVariantImagesSuccessfully() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(storageService.store(any())).thenReturn("cover.jpg");
        when(storageService.storeAll(any())).thenReturn(List.of("img1.jpg", "img2.jpg"));

        assertDoesNotThrow(() -> handler.handle(command));

        verify(productRepository).save(product);
        verify(eventPublisher).publish(product);
        verify(cleanupService).cleanupUnusedFiles(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        var productId = UUID.randomUUID();
        var command = validCommand(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> handler.handle(command));
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldUploadOnlyCoverImage() {
        var productId = UUID.randomUUID();
        var product = createProduct();
        var command = new UploadVariantImagesCommand(
            productId,
            "SKU-001",
            createFileUpload("cover.jpg"),
            null
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(storageService.store(any())).thenReturn("cover.jpg");

        assertDoesNotThrow(() -> handler.handle(command));

        verify(storageService).store(any());
        verify(storageService, never()).storeAll(any());
    }

    private Product createProduct() {
        var variant = new ProductVariant(
            new SKU("SKU-001"),
            "Variant Name",
            new Money(BigDecimal.valueOf(100), "BRL")
        );
        return new Product("Product Name", "Description", List.of(variant));
    }

    private UploadVariantImagesCommand validCommand(UUID productId) {
        return new UploadVariantImagesCommand(
            productId,
            "SKU-001",
            createFileUpload("cover.jpg"),
            List.of(createFileUpload("img1.jpg"), createFileUpload("img2.jpg"))
        );
    }

    private FileUpload createFileUpload(String filename) {
        return new FileUpload(
            filename,
            () -> new ByteArrayInputStream(new byte[0]),
            "image/jpeg",
            1024L
        );
    }
}
