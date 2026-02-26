package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductVariantTest {

    @Test
    void shouldCreateVariantWithValidData() {
        var variant = new ProductVariant(
                new SKU("SKU-001"),
                "Variant Name",
                new Money(BigDecimal.valueOf(100), "BRL")
        );

        assertNotNull(variant);
        assertEquals("Variant Name", variant.getName());
    }

    @Test
    void shouldThrowExceptionWhenSKUIsNull() {
        assertThrows(BusinessError.class, () -> new ProductVariant(
                null,
                "Variant Name",
                new Money(BigDecimal.valueOf(100), "BRL")
        ));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(BusinessError.class, () -> new ProductVariant(
                new SKU("SKU-001"),
                null,
                new Money(BigDecimal.valueOf(100), "BRL")
        ));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(BusinessError.class, () -> new ProductVariant(
                new SKU("SKU-001"),
                "",
                new Money(BigDecimal.valueOf(100), "BRL")
        ));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThrows(BusinessError.class, () -> new ProductVariant(
                new SKU("SKU-001"),
                "Variant Name",
                null
        ));
    }

    @Test
    void shouldUpdateCoverImage() {
        var variant = validVariant();
        variant.updateCoverImage("path/to/image.jpg");

        assertEquals("path/to/image.jpg", variant.getCoverImage());
    }

    @Test
    void shouldUpdateGallery() {
        var variant = validVariant();
        var gallery = List.of("image1.jpg", "image2.jpg");
        variant.updateGallery(gallery);

        assertEquals(gallery, variant.getGallery());
    }

    @Test
    void shouldUpdateDetails() {
        var variant = validVariant();
        Map<String, Object> details = Map.of("color", "red", "size", "M");
        variant.updateDetails(details);

        assertEquals(details, variant.getDetails());
    }

    private ProductVariant validVariant() {
        return new ProductVariant(
                new SKU("SKU-001"),
                "Variant Name",
                new Money(BigDecimal.valueOf(100), "BRL")
        );
    }
}
