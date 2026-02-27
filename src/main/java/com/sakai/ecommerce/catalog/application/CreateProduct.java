package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.commands.CreateProductWithVariantsCommand;
import com.sakai.ecommerce.catalog.application.commands.CreateVariantCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.DuplicateSKUException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProduct {
    private final VariantMapper variantMapper;
    private final ProductCleanupService cleanupService;
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;
    private final StorageService storageService;

    @Transactional
    public UUID handle(CreateProductWithVariantsCommand command) {
        validateUniqueSKUs(command.variants());

        var variants = command.variants().stream()
                .map(this::mapVariantWithUpload)
                .toList();

        try {
            var product = createProduct(command, variants);
            var productId = productRepository.save(product);
            eventPublisher.publish(product);

            return productId;
        } catch (Exception exception) {
            cleanupService.cleanupVariantFiles(variants);
            throw exception;
        }
    }

    private ProductVariant mapVariantWithUpload(CreateVariantCommand command) {
        var coverImagePath = Optional.ofNullable(command.coverImage())
                .map(storageService::store)
                .orElse(null);

        var galleryPaths = Optional.ofNullable(command.gallery())
                .map(storageService::storeAll)
                .orElse(List.of());

        return variantMapper.map(command, coverImagePath, galleryPaths);
    }

    private Product createProduct(CreateProductWithVariantsCommand command, List<ProductVariant> variants) {
        var product = new Product(
            command.name(),
            command.description(),
            variants
        );

        updateDimensions(product, command.dimensions());
        return product;
    }

    private void validateUniqueSKUs(List<CreateVariantCommand> variants) {
        var skus = variants.stream()
                .map(CreateVariantCommand::sku)
                .toList();

        var hasDuplicates = skus.size() != skus.stream().distinct().count();

        if (hasDuplicates) {
            throw new DuplicateSKUException();
        }
    }

    private void updateDimensions(Product product, ProductDimensionsDTO dimensions) {
        if (dimensions == null) return;

        var newProductDimensions = new ProductDimensions(
            dimensions.height(),
            dimensions.width(),
            dimensions.depth(),
            dimensions.weight()
        );

        product.updateDimensions(newProductDimensions);
    }
}
