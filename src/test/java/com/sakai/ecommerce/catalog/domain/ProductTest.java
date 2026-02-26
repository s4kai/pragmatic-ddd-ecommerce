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
