package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.dto.CartResponse;
import com.sakai.ecommerce.cart.application.queries.GetCartQuery;
import com.sakai.ecommerce.cart.domain.CartItem;
import com.sakai.ecommerce.catalog.ProductInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCartHandler {
    private final CartRetriever cartRetriever;
    private final ProductInfoService productInfoService;

    public CartResponse handle(GetCartQuery query) {
        var cart = cartRetriever.getCart(query.userId(), query.sessionId());

        var items = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();
        
        return new CartResponse(
                cart.getId(),
                cart.getCustomerId(),
                cart.getSessionId(),
                cart.getStatus().name(),
                items,
                cart.getTotal()
        );
    }

    private CartResponse.CartItemResponse toCartItemResponse(CartItem item) {
        var productInfo = productInfoService.getProductInfo(item.getProductId(), item.getSku())
                .map(this::toProductInfo)
                .orElse(null);

        return new CartResponse.CartItemResponse(
                item.getProductId(),
                item.getSku(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal(),
                productInfo
        );
    }

    private CartResponse.ProductInfo toProductInfo(ProductInfoService.ProductInfo info) {
        return new CartResponse.ProductInfo(
                info.productName(),
                info.variantName(),
                info.coverImage()
        );
    }
}
