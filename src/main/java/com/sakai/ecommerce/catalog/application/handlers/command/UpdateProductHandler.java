package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.ProductCleanupService;
import com.sakai.ecommerce.catalog.application.commands.UpdateProductWithVariantsCommand;
import com.sakai.ecommerce.catalog.application.commands.UpdateVariantCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.application.exception.StorageException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.application.services.StorageService;
import com.sakai.ecommerce.shared.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateProductHandler {
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;
    private final StorageService storageService;
    private final ProductCleanupService cleanupService;

    @Transactional
    public void handle(UpdateProductWithVariantsCommand command) {
        var product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        var skus = command.variants().stream().map(v -> new SKU(v.sku())).toList();
        product.validateUniqueSKUs(skus);

        var oldFiles = product.getVariantFiles();

        try {
            updateProductInfo(product, command);
            updateVariants(product, command.variants());
            updateDimensions(product, command.dimensions());

            productRepository.save(product);
            eventPublisher.publish(product);

            cleanupService.cleanupUnusedFiles(oldFiles, product.getVariantFiles());
        } catch (StorageException exception) {
            cleanupService.cleanupVariantFiles(product.getProductVariants());
            throw exception;
        }
    }

    private void updateProductInfo(Product product, UpdateProductWithVariantsCommand command) {
        if (command.name() != null && !command.name().isBlank()) {
            product.updateName(command.name());
        }
        if (command.description() != null && !command.description().isBlank()) {
            product.updateDescription(command.description());
        }
    }

    private void updateVariants(Product product, List<UpdateVariantCommand> variantCommands) {
        for (var command : variantCommands) {
            var variant = product.findVariantBySKU(new SKU(command.sku()));
            updateVariant(variant, command);
        }
    }

    private void updateVariant(ProductVariant variant, UpdateVariantCommand command) {
        var price = (command.price() != null && command.currency() != null) 
                ? new Money(command.price(), command.currency()) 
                : null;
        
        var coverImage = command.coverImage() != null 
                ? storageService.store(command.coverImage()) 
                : null;
        
        var gallery = command.gallery() != null 
                ? storageService.storeAll(command.gallery()) 
                : null;

        variant.update(command.name(), price, coverImage, gallery, command.details());
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
