package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    @Test
    void shouldCreateSKUWithValidValue() {
        var sku = new SKU("SKU-001");

        assertNotNull(sku);
        assertEquals("SKU-001", sku.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(BusinessError.class, () -> new SKU(null));
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        assertThrows(BusinessError.class, () -> new SKU(""));
        assertThrows(BusinessError.class, () -> new SKU("   "));
    }

    @Test
    void shouldBeEqualWhenValuesAreTheSame() {
        var sku1 = new SKU("SKU-001");
        var sku2 = new SKU("SKU-001");

        assertEquals(sku1, sku2);
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        var sku1 = new SKU("SKU-001");
        var sku2 = new SKU("SKU-002");

        assertNotEquals(sku1, sku2);
    }
}
