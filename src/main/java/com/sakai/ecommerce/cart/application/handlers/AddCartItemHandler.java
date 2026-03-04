package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.AddItemToCartCommand;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.catalog.ProductPricingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddCartItemHandler {
    private final CartRepository cartRepository;
    private final ProductPricingService productPricingService;
    private final CartRetriever cartRetriever;

    @Transactional
    public void addItem(AddItemToCartCommand command) {
        var customerId = command.customerId();
        var sessionId = command.sessionId();

        var cart = cartRetriever.getCart(customerId, sessionId);
        var price = productPricingService.getPrice(command.productId(), command.sku());

        cart.addItem(command.productId(), command.sku(), command.quantity(), price);

        cartRepository.save(cart);
    }
}
