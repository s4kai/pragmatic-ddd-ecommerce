package com.sakai.ecommerce.catalog.application;

import com.sakai.ecommerce.catalog.application.commands.CreateVariantCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VariantMapperTest {

    private final VariantMapper mapper = new VariantMapper();

    @Test
    void shouldMapCommandToVariant() {
        var command = new CreateVariantCommand(
                "SKU-001",
                "Variant Name",
                null,
                BigDecimal.valueOf(100),
                "BRL",
                null,
                Map.of("color", "red")
        );

        var variant = mapper.map(command, "cover.jpg", List.of("img1.jpg", "img2.jpg"));

        assertNotNull(variant);
        assertEquals("SKU-001", variant.getSku().getValue());
        assertEquals("Variant Name", variant.getName());
        assertEquals("cover.jpg", variant.getCoverImage());
        assertEquals(2, variant.getGallery().size());
        assertEquals(Map.of("color", "red"), variant.getDetails());
    }

    @Test
    void shouldMapCommandWithNullDetails() {
        var command = new CreateVariantCommand(
                "SKU-002",
                "Variant",
                null,
                BigDecimal.valueOf(50),
                "USD",
                null,
                null
        );

        var variant = mapper.map(command, null, List.of());

        assertNotNull(variant);
        assertNull(variant.getDetails());
        assertNull(variant.getCoverImage());
        assertTrue(variant.getGallery().isEmpty());
    }
}
