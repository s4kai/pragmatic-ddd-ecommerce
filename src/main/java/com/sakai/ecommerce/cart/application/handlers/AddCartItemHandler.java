package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.AddItemToCartCommand;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.catalog.ProductPricingService;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddCartItemHandler {
    private final CartRepository cartRepository;
    private final ProductPricingService productPricingService;
    private final CartRetriever cartRetriever;
    private final EventPublisher eventPublisher;

    @Transactional
    public void addItem(AddItemToCartCommand command) {
        var cart = cartRetriever.getCart();
        var price = productPricingService.getPrice(command.productId(), command.sku());

        cart.addItem(command.productId(), command.sku(), command.quantity(), price);

        cartRepository.save(cart);
        eventPublisher.publish(cart);
    }
}
