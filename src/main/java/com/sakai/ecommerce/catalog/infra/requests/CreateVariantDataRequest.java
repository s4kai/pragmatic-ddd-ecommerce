package com.sakai.ecommerce.catalog.infra.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakai.ecommerce.catalog.application.commands.CreateVariantDataCommand;

import java.math.BigDecimal;
import java.util.HashMap;

public record CreateVariantDataRequest(
        String sku,
        String name,
        BigDecimal price,
        String currency,
        String details
) {
    public CreateVariantDataCommand toCommand() {
        return new CreateVariantDataCommand(
                sku,
                name,
                price,
                currency,
                details == null ? null : parseJson(details)
        );
    }

    private HashMap<String, Object> parseJson(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (Exception e) {
            return null;
        }
    }
}
