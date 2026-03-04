package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.core.BaseEntity;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductGallery extends BaseEntity<String> {
    @ManyToOne
    private ProductVariant productVariant;

    public ProductGallery(String externalURL) {
        if(externalURL == null || externalURL.isBlank()) throw new BusinessError("A external URL cannot be null.");
        this.id = externalURL;
    }

    @EqualsAndHashCode.Include
    public String getId() {
        return id;
    }
}
