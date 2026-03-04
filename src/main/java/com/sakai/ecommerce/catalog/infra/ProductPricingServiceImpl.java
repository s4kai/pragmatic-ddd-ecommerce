package com.sakai.ecommerce.catalog.infra;

import com.sakai.ecommerce.catalog.ProductPricingService;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.SKU;
import com.sakai.ecommerce.shared.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductPricingServiceImpl implements ProductPricingService {
    private final ProductRepository productRepository;

    @Override
    public Money getPrice(UUID productId, String sku) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        
       var variant =  product.findVariantBySKU(new SKU(sku));
       
       return variant.getPrice();
    }
}
