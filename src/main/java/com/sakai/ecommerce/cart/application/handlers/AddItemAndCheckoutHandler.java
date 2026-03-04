package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.AddItemAndCheckoutCommand;
import com.sakai.ecommerce.cart.application.dto.CartCheckoutResponse;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.catalog.ProductPricingService;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddItemAndCheckoutHandler {
    private final CartRepository cartRepository;
    private final ProductPricingService productPricingService;
    private final CartRetriever cartRetriever;

    @Transactional
    public CartCheckoutResponse addItemAndCheckout(AddItemAndCheckoutCommand command) {
        var cart = cartRetriever.getCart(command.customerId(), command.sessionId());
        var price = productPricingService.getPrice(command.productId(), command.sku());

        cart.addItem(command.productId(), command.sku(), command.quantity(), price);

        cartRepository.save(cart);

        return new CartCheckoutResponse(cart.getId());
    }
}
