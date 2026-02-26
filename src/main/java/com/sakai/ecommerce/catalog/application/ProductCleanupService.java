package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.shared.application.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProductCleanupService {
    private final StorageService storageService;

    void cleanupVariantFiles(List<ProductVariant> variants) {
        var allFiles = collectAllFiles(variants);
        storageService.deleteAll(allFiles);
    }

    private List<String> collectAllFiles(List<ProductVariant> variants) {
        List<String> files = new ArrayList<>();

        variants.stream()
                .map(ProductVariant::getCoverImage)
                .filter(Objects::nonNull)
                .forEach(files::add);

        variants.stream()
                .map(ProductVariant::getGallery)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .forEach(files::add);

        return files;
    }
}