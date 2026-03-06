package com.sakai.ecommerce.catalog.application.handlers.command;

import com.sakai.ecommerce.catalog.application.commands.UpdateProductCommand;
import com.sakai.ecommerce.catalog.application.commands.UpdateVariantDataCommand;
import com.sakai.ecommerce.catalog.application.dto.ProductDimensionsDTO;
import com.sakai.ecommerce.catalog.domain.*;
import com.sakai.ecommerce.catalog.domain.exception.ProductNotFoundException;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
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

    @Transactional
    public void handle(UpdateProductCommand command) {
        var product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException(command.productId()));

        var skus = command.variants().stream().map(v -> new SKU(v.sku())).toList();
        product.validateUniqueSKUs(skus);

        updateProductInfo(product, command);
        updateVariants(product, command.variants());
        updateDimensions(product, command.dimensions());

        productRepository.save(product);
        eventPublisher.publish(product);
    }

    private void updateProductInfo(Product product, UpdateProductCommand command) {
        if (command.name() != null && !command.name().isBlank()) {
            product.updateName(command.name());
        }
        if (command.description() != null && !command.description().isBlank()) {
            product.updateDescription(command.description());
        }
    }

    private void updateVariants(Product product, List<UpdateVariantDataCommand> variantCommands) {
        for (var command : variantCommands) {
            var variant = product.findVariantBySKU(new SKU(command.sku()));
            updateVariant(variant, command);
        }
    }

    private void updateVariant(ProductVariant variant, UpdateVariantDataCommand command) {
        var price = (command.price() != null && command.currency() != null) 
                ? new Money(command.price(), command.currency()) 
                : null;

        variant.updateData(command.name(), price, command.details());
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
