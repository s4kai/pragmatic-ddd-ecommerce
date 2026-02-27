package com.sakai.ecommerce.catalog.application.handlers;

import com.sakai.ecommerce.shared.application.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductVariantFilesCleanupHandler {
    private final StorageService storageService;

    public void cleanupUnusedFiles(List<String> oldFiles, List<String> newFiles) {
        oldFiles.stream()
                .filter(file -> !newFiles.contains(file))
                .forEach(storageService::delete);
    }
}
