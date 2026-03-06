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

    @Test
    void shouldUpdateName() {
        var variant = validVariant();
        variant.updateName("New Name");

        assertEquals("New Name", variant.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithNull() {
        var variant = validVariant();
        assertThrows(BusinessError.class, () -> variant.updateName(null));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNameWithBlank() {
        var variant = validVariant();
        assertThrows(BusinessError.class, () -> variant.updateName(""));
    }

    @Test
    void shouldUpdatePrice() {
        var variant = validVariant();
        var newPrice = new Money(BigDecimal.valueOf(200), "BRL");
        variant.updatePrice(newPrice);

        assertEquals(newPrice, variant.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingPriceWithNull() {
        var variant = validVariant();
        assertThrows(BusinessError.class, () -> variant.updatePrice(null));
    }

    @Test
    void shouldUpdateAllFieldsAtOnce() {
        var variant = validVariant();
        var newPrice = new Money(BigDecimal.valueOf(150), "BRL");
        Map<String, Object> details = Map.of("color", "blue");

        variant.updateData("Updated Name", newPrice, details);

        assertEquals("Updated Name", variant.getName());
        assertEquals(newPrice, variant.getPrice());
        assertEquals(details, variant.getDetails());
    }

    @Test
    void shouldUpdateOnlyNonNullFields() {
        var variant = validVariant();
        var originalPrice = variant.getPrice();

        variant.updateData("New Name", null, null);

        assertEquals("New Name", variant.getName());
        assertEquals(originalPrice, variant.getPrice());
    }

    @Test
    void shouldUpdateImages() {
        var variant = validVariant();
        var gallery = List.of("img1.jpg", "img2.jpg");

        variant.updateImages("cover.jpg", gallery);

        assertEquals("cover.jpg", variant.getCoverImage());
        assertEquals(gallery, variant.getGallery());
    }

    @Test
    void shouldReturnTrueWhenSKUMatches() {
        var variant = validVariant();
        assertTrue(variant.hasSameSKU(new SKU("SKU-001")));
    }

    @Test
    void shouldReturnFalseWhenSKUDoesNotMatch() {
        var variant = validVariant();
        assertFalse(variant.hasSameSKU(new SKU("SKU-002")));
    }

    @Test
    void shouldReturnFilesWithCoverImageAndGallery() {
        var variant = validVariant();
        variant.updateCoverImage("cover.jpg");
        variant.updateGallery(List.of("img1.jpg", "img2.jpg"));

        var files = variant.getFiles();

        assertEquals(3, files.size());
        assertTrue(files.contains("cover.jpg"));
        assertTrue(files.contains("img1.jpg"));
        assertTrue(files.contains("img2.jpg"));
    }

    @Test
    void shouldReturnOnlyCoverImageWhenGalleryIsEmpty() {
        var variant = validVariant();
        variant.updateCoverImage("cover.jpg");

        var files = variant.getFiles();

        assertEquals(1, files.size());
        assertEquals("cover.jpg", files.getFirst());
    }

    @Test
    void shouldReturnEmptyListWhenNoFiles() {
        var variant = validVariant();

        var files = variant.getFiles();

        assertTrue(files.isEmpty());
    }

    private ProductVariant validVariant() {
        return new ProductVariant(
                new SKU("SKU-001"),
                "Variant Name",
                new Money(BigDecimal.valueOf(100), "BRL")
        );
    }
}
