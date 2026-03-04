package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDimensionsTest {

    @Test
    void shouldCreateDimensionsWithValidValues() {
        var dimensions = new ProductDimensions(10.0, 20.0, 30.0, 5.0);

        assertNotNull(dimensions);
    }

    @Test
    void shouldThrowExceptionWhenHeightIsZeroOrNegative() {
        assertThrows(BusinessError.class, () -> new ProductDimensions(0, 20.0, 30.0, 5.0));
        assertThrows(BusinessError.class, () -> new ProductDimensions(-1, 20.0, 30.0, 5.0));
    }

    @Test
    void shouldThrowExceptionWhenWidthIsZeroOrNegative() {
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, 0, 30.0, 5.0));
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, -1, 30.0, 5.0));
    }

    @Test
    void shouldThrowExceptionWhenDepthIsZeroOrNegative() {
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, 20.0, 0, 5.0));
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, 20.0, -1, 5.0));
    }

    @Test
    void shouldThrowExceptionWhenWeightIsZeroOrNegative() {
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, 20.0, 30.0, 0));
        assertThrows(BusinessError.class, () -> new ProductDimensions(10.0, 20.0, 30.0, -1));
    }

    @Test
    void shouldGetAllDimensionValues() {
        var dimensions = new ProductDimensions(10.0, 20.0, 30.0, 5.0);

        assertEquals(10.0, dimensions.getHeight());
        assertEquals(20.0, dimensions.getWidth());
        assertEquals(30.0, dimensions.getDepth());
        assertEquals(5.0, dimensions.getWeight());
    }
}
