package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.DecreaseItemQuantityCommand;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecreaseCartItemQuantityHandler {
    private final CartRepository cartRepository;
    private final CartRetriever cartRetriever;
    private final EventPublisher eventPublisher;

    @Transactional
    public void decreaseQuantity(DecreaseItemQuantityCommand command) {
        var cart = cartRetriever.getCart();
        
        cart.decreaseItemQuantity(command.sku(), command.quantity());
        
        cartRepository.save(cart);
        eventPublisher.publish(cart);
    }
}
