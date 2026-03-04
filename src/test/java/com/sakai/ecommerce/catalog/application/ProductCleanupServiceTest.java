package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.shared.application.services.StorageService;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCleanupServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ProductCleanupService cleanupService;

    @Test
    void shouldCleanupVariantFiles() {
        var variant = createVariant();
        variant.updateCoverImage("cover.jpg");
        variant.updateGallery(List.of("img1.jpg", "img2.jpg"));

        cleanupService.cleanupVariantFiles(List.of(variant));

        verify(storageService).deleteAll(argThat(files -> 
            files.size() == 3 && 
            files.contains("cover.jpg") && 
            files.contains("img1.jpg") && 
            files.contains("img2.jpg")
        ));
    }

    @Test
    void shouldCleanupUnusedFiles() {
        var oldFiles = List.of("old1.jpg", "old2.jpg", "keep.jpg");
        var newFiles = List.of("keep.jpg", "new.jpg");

        cleanupService.cleanupUnusedFiles(oldFiles, newFiles);

        verify(storageService).delete("old1.jpg");
        verify(storageService).delete("old2.jpg");
        verify(storageService, never()).delete("keep.jpg");
    }

    @Test
    void shouldHandleEmptyVariantList() {
        cleanupService.cleanupVariantFiles(List.of());

        verify(storageService).deleteAll(List.of());
    }

    private ProductVariant createVariant() {
        return new ProductVariant(
                new SKU("SKU-001"),
                "Variant",
                new Money(BigDecimal.valueOf(100), "BRL")
        );
    }
}
