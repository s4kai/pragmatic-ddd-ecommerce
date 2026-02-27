package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
public class ProductVariant {
    @EmbeddedId
    private SKU sku;

    private String name;
    private String coverImage;

    @Embedded
    private Money price;

    @ElementCollection
    private List<String> gallery;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> details;

    public ProductVariant(SKU sku, String name, Money price){
        validateCreate(sku, name, price);

        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    public void update(String name, Money price, String coverImage, List<String> gallery, Map<String, Object> details) {
        if (name != null) updateName(name);
        if (price != null) updatePrice(price);
        if (coverImage != null) updateCoverImage(coverImage);
        if (gallery != null) updateGallery(gallery);
        if (details != null) updateDetails(details);
    }

    public void updateCoverImage(String coverImage){
        this.coverImage = coverImage;
    }

    public void updateDetails(Map<String, Object> details){
        this.details = details;
    }

    public void updateGallery(List<String> gallery){
        this.gallery = gallery;
    }

    boolean hasSameSKU(SKU sku){
        return this.sku.equals(sku);
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) throw new BusinessError("Name cannot be null or blank");
        this.name = name;
    }

    public void updatePrice(Money price) {
        if (price == null) throw new BusinessError("Price cannot be null");
        this.price = price;
    }

    public List<String> getFiles() {
        var files = new ArrayList<String>();
        if (coverImage != null) files.add(coverImage);
        if (gallery != null) files.addAll(gallery);
        return files;
    }

    private void validateCreate(SKU sku, String name, Money price){
        if(sku == null) throw new BusinessError("SKU cannot be null");
        if(name == null || name.isBlank()) throw new BusinessError("Name cannot be null or blank");
        if(price == null) throw new BusinessError("Price cannot be null");
    }
}
