package com.sakai.ecommerce.catalog.infra;

import com.sakai.ecommerce.catalog.ProductInfoService;
import com.sakai.ecommerce.catalog.domain.ProductRepository;
import com.sakai.ecommerce.catalog.domain.SKU;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductInfoServiceImpl implements ProductInfoService {
    private final ProductRepository productRepository;

    @Override
    public Optional<ProductInfo> getProductInfo(UUID productId, String sku) {
        return productRepository.findById(productId)
                .flatMap(product -> {
                    var variant = product.findVariantBySKU(new SKU(sku));
                    return Optional.of(new ProductInfo(
                            product.getName(),
                            variant.getName(),
                            variant.getCoverImage()
                    ));
                });
    }
}
