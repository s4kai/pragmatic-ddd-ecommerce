package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.commands.CreateVariantCommand;
import com.sakai.ecommerce.catalog.domain.ProductVariant;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.shared.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class VariantMapper {
    ProductVariant map(CreateVariantCommand command, String coverImagePath, List<String> galleryPaths) {
        var variant = new ProductVariant(
                new SKU(command.sku()),
                command.name(),
                new Money(command.price(), command.currency())
        );

        variant.updateDetails(command.details());
        variant.updateGallery(galleryPaths);
        variant.updateCoverImage(coverImagePath);

        return variant;
    }
}