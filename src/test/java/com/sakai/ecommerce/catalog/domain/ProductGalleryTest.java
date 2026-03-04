package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductGalleryTest {

    @Test
    void shouldCreateGalleryWithValidURL() {
        var gallery = new ProductGallery("https://example.com/image.jpg");

        assertNotNull(gallery);
        assertEquals("https://example.com/image.jpg", gallery.getId());
    }

    @Test
    void shouldThrowExceptionWhenURLIsNull() {
        assertThrows(BusinessError.class, () -> new ProductGallery(null));
    }

    @Test
    void shouldThrowExceptionWhenURLIsBlank() {
        assertThrows(BusinessError.class, () -> new ProductGallery(""));
        assertThrows(BusinessError.class, () -> new ProductGallery("   "));
    }

    @Test
    void shouldBeEqualWhenURLsAreTheSame() {
        var gallery1 = new ProductGallery("image.jpg");
        var gallery2 = new ProductGallery("image.jpg");

        assertEquals(gallery1, gallery2);
    }

    @Test
    void shouldNotBeEqualWhenURLsAreDifferent() {
        var gallery1 = new ProductGallery("image1.jpg");
        var gallery2 = new ProductGallery("image2.jpg");

        assertNotEquals(gallery1, gallery2);
    }
}
