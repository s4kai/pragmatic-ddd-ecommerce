package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.commands.CreateProductCommand;
import com.sakai.ecommerce.catalog.application.commands.CreateVariantDataCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.DuplicateSKUException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProductHandler {
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public UUID handle(CreateProductCommand command) {
        validateUniqueSKUs(command.variants());

        var variants = command.variants().stream()
                .map(this::mapVariant)
                .toList();

        var product = new Product(
            command.name(),
            command.description(),
            variants
        );

        updateDimensions(product, command.dimensions());
        
        var productId = productRepository.save(product);
        eventPublisher.publish(product);

        return productId;
    }

    private ProductVariant mapVariant(CreateVariantDataCommand command) {
        var sku = new SKU(command.sku());
        var price = new Money(command.price(), command.currency());
        var variant = new ProductVariant(sku, command.name(), price);
        
        if (command.details() != null) {
            variant.updateDetails(command.details());
        }
        
        return variant;
    }

    private void validateUniqueSKUs(List<CreateVariantDataCommand> variants) {
        var skus = variants.stream()
                .map(CreateVariantDataCommand::sku)
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
