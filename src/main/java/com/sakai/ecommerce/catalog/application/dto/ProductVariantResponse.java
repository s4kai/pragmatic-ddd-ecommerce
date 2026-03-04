package com.sakai.ecommerce.catalog.application.dto;

import com.sakai.ecommerce.catalog.domain.ProductGallery;
import com.sakai.ecommerce.catalog.domain.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ProductVariantResponse {
    private String sku;
    private String name;
    private BigDecimal price;
    private String currency;
    private String coverImage;
    private List<String> gallery;
    private Map<String, Object> details;

    public static ProductVariantResponse fromDomain(ProductVariant variant) {
        return new ProductVariantResponse(
                variant.getSku().getValue(),
                variant.getName(),
                variant.getPrice().getAmount(),
                variant.getPrice().getCurrency(),
                variant.getCoverImage(),
                variant.getGallery().stream()
                        .map(ProductGallery::getId)
                        .toList(),
                variant.getDetails()
        );
    }
}
