package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.RemoveItemFromCartCommand;
import com.sakai.ecommerce.cart.domain.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveCartItemHandler {
    private final CartRepository cartRepository;
    private final CartRetriever cartRetriever;

    @Transactional
    public void removeItem(RemoveItemFromCartCommand command) {
        var cart = cartRetriever.getCart(command.customerId(), command.sessionId());
        
        cart.removeItem(command.sku());
        
        cartRepository.save(cart);
    }
}
