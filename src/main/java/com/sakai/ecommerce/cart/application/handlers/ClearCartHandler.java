package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.domain.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClearCartHandler {
    private final CartRetriever cartRetriever;
    private final CartRepository cartRepository;

    public void handle() {
        var cart = cartRetriever.getCart();
        cart.clear();
        cartRepository.save(cart);
    }
}
