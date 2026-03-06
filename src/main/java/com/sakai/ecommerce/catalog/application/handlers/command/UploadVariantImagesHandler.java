package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.ProductCleanupService;
import com.sakai.ecommerce.catalog.application.commands.UploadVariantImagesCommand;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.application.exception.StorageException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadVariantImagesHandler {
    private final ProductRepository productRepository;
    private final StorageService storageService;
    private final ProductCleanupService cleanupService;
    private final EventPublisher eventPublisher;

    @Transactional
    public void handle(UploadVariantImagesCommand command) {
        var product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        var variant = product.findVariantBySKU(new SKU(command.sku()));
        var oldFiles = variant.getFiles();

        var coverImagePath = Optional.ofNullable(command.coverImage())
                .map(storageService::store)
                .orElse(null);

        var galleryPaths = Optional.ofNullable(command.gallery())
                .map(storageService::storeAll)
                .orElse(null);

        variant.updateImages(coverImagePath, galleryPaths);

        productRepository.save(product);
        eventPublisher.publish(product);

        cleanupService.cleanupUnusedFiles(oldFiles, variant.getFiles());
    }
}
