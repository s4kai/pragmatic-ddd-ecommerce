package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Product extends AggregateRoot<UUID> {
    private String name;
    private String description;

    @Embedded
    private ProductDimensions dimensions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> productVariants;

    public List<ProductVariant> getProductVariants(){
        return Collections.unmodifiableList(this.productVariants);
    }

    public Product(String name, String description, List<ProductVariant> productVariants) {
        validateCreateProduct(name, description, productVariants);

        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.productVariants = productVariants;
    }

    private void validateCreateProduct(String name, String description, List<ProductVariant> productVariants) {
        if (name == null || name.isBlank()) throw new BusinessError("Product name cannot be null or blank");

        if (description == null || description.isBlank())
            throw new BusinessError("Product description cannot be null or blank");

        if (productVariants == null || productVariants.isEmpty())
            throw new BusinessError("Product must have at least one variant");
    }

    public void updateDimensions(ProductDimensions newDimensions) {
        this.dimensions = newDimensions;
    }

    public void addVariant(ProductVariant newVariant) {
        var sku = newVariant.getSku();
        var alreadyHasThisVariant = this.productVariants
                .stream().anyMatch(
                variant -> variant.hasSameSKU(sku)
        );

        if (alreadyHasThisVariant) throw new BusinessError("Product already has a variant with this SKU");

        this.productVariants.add(newVariant);
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) throw new BusinessError("Product name cannot be null or blank");
        this.name = name;
    }

    public void updateDescription(String description) {
        if (description == null || description.isBlank())
            throw new BusinessError("Product description cannot be null or blank");

        this.description = description;
    }

    public ProductVariant findVariantBySKU(SKU sku) {
        return productVariants.stream()
                .filter(v -> v.hasSameSKU(sku))
                .findFirst()
                .orElseThrow(() -> new BusinessError("Variant not found: " + sku.getValue()));
    }

    public void validateUniqueSKUs(List<SKU> skus) {
        var hasDuplicates = skus.size() != skus.stream().distinct().count();
        if (hasDuplicates) {
            throw new BusinessError("Duplicate SKUs found in variants");
        }
    }

    public List<String> getVariantFiles() {
        return productVariants.stream()
                .flatMap(v -> v.getFiles().stream())
                .toList();
    }
}
