package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        var product = new Product(
                "Product Name",
                "Product Description",
                List.of(validVariant())
        );

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Product Name", product.getName());
        assertEquals(1, product.getProductVariants().size());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(BusinessError.class, () -> new Product(
                null,
                "Description",
                List.of(validVariant())
        ));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(BusinessError.class, () -> new Product(
                "",
                "Description",
                List.of(validVariant())
        ));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThrows(BusinessError.class, () -> new Product(
                "Product Name",
                null,
                List.of(validVariant())
        ));
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        assertThrows(BusinessError.class, () -> new Product(
                "Product Name",
                "",
                List.of(validVariant())
        ));
    }

    @Test
    void shouldThrowExceptionWhenVariantsIsNull() {
        assertThrows(BusinessError.class, () -> new Product(
                "Product Name",
                "Description",
                null
        ));
    }

    @Test
    void shouldThrowExceptionWhenVariantsIsEmpty() {
        assertThrows(BusinessError.class, () -> new Product(
                "Product Name",
                "Description",
                List.of()
        ));
    }

    @Test
    void shouldUpdateDimensions() {
        var product = validProduct();
        var dimensions = new ProductDimensions(10.0, 20.0, 30.0, 5.0);

        product.updateDimensions(dimensions);

        assertEquals(dimensions, product.getDimensions());
    }

    @Test
    void shouldAddNewVariant() {
        var product = validProduct();
        var newVariant = new ProductVariant(
                new SKU("SKU-002"),
                "New Variant",
                new Money(BigDecimal.valueOf(200), "BRL")
        );

        product.addVariant(newVariant);

        assertEquals(2, product.getProductVariants().size());
    }

    @Test
    void shouldThrowExceptionWhenAddingVariantWithDuplicateSKU() {
        var product = validProduct();
        var duplicateVariant = new ProductVariant(
            new SKU("SKU-001"),
            "Duplicate Variant",
            new Money(BigDecimal.valueOf(200), "BRL")
        );

        assertThrows(BusinessError.class, () -> product.addVariant(duplicateVariant));
    }

    @Test
    void shouldUpdateName() {
        var product = validProduct();
        product.updateName("New Name");

        assertEquals("New Name", product.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithNull() {
        var product = validProduct();
        assertThrows(BusinessError.class, () -> product.updateName(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithBlank() {
        var product = validProduct();
        assertThrows(BusinessError.class, () -> product.updateName(""));
    }

    @Test
    void shouldUpdateDescription() {
        var product = validProduct();
        product.updateDescription("New Description");

        assertEquals("New Description", product.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithNull() {
        var product = validProduct();
        assertThrows(BusinessError.class, () -> product.updateDescription(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDescriptionWithBlank() {
        var product = validProduct();
        assertThrows(BusinessError.class, () -> product.updateDescription(""));
    }

    @Test
    void shouldFindVariantBySKU() {
        var product = validProduct();
        var variant = product.findVariantBySKU(new SKU("SKU-001"));

        assertNotNull(variant);
        assertEquals("Variant Name", variant.getName());
    }

    @Test
    void shouldThrowExceptionWhenVariantNotFound() {
        var product = validProduct();
        assertThrows(BusinessError.class, () -> product.findVariantBySKU(new SKU("SKU-999")));
    }

    @Test
    void shouldValidateUniqueSKUs() {
        var product = validProduct();
        var skus = List.of(new SKU("SKU-001"), new SKU("SKU-002"));

        assertDoesNotThrow(() -> product.validateUniqueSKUs(skus));
    }

    @Test
    void shouldThrowExceptionWhenSKUsAreDuplicated() {
        var product = validProduct();
        var skus = List.of(new SKU("SKU-001"), new SKU("SKU-001"));

        assertThrows(BusinessError.class, () -> product.validateUniqueSKUs(skus));
    }

    @Test
    void shouldGetVariantFiles() {
        var product = validProduct();
        var variant = product.getProductVariants().get(0);
        variant.updateCoverImage("cover.jpg");
        variant.updateGallery(List.of("img1.jpg", "img2.jpg"));

        var files = product.getVariantFiles();

        assertEquals(3, files.size());
        assertTrue(files.contains("cover.jpg"));
    }

    @Test
    void shouldReturnUnmodifiableListOfVariants() {
        var product = validProduct();
        var variants = product.getProductVariants();

        assertThrows(UnsupportedOperationException.class, () -> 
            variants.add(validVariant())
        );
    }

    private Product validProduct() {
        return new Product(
                "Product Name",
                "Product Description",
                new ArrayList<>(List.of(validVariant()))
        );
    }

    private ProductVariant validVariant() {
        return new ProductVariant(
                new SKU("SKU-001"),
                "Variant Name",
                new Money(BigDecimal.valueOf(100), "BRL")
        );
    }
}
