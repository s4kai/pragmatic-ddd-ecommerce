package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class SKU {
    private String value;

    public SKU(String value) {
        if(value == null || value.isBlank()) throw new BusinessError("SKU cannot be null or blank");

        this.value = value;
    }
}
