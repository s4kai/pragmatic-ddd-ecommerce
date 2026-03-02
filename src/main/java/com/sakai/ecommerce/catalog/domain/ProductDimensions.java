package com.sakai.ecommerce.catalog.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ProductDimensions {
    private double height;
    private double width;
    private double depth;
    private double weight;

    public ProductDimensions(double height, double width, double depth, double weight) {
        if(height <= 0 || width <= 0 || depth <= 0 || weight <= 0)
            throw new BusinessError("All dimensions must be positive numbers");

        this.height = height;
        this.width = width;
        this.depth = depth;
        this.weight = weight;
    }
}
