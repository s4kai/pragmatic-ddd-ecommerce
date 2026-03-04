package com.sakai.ecommerce.cart.application;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartRetriever {
    private final CartRepository cartRepository;
    
    public Cart getCart(UUID customerId, String sessionId) {
        if (customerId != null) {
            return cartRepository
                    .findByCustomerId(customerId)
                    .orElse(new Cart(customerId));
        }

        if (sessionId != null) {
            return cartRepository
                    .findBySessionId(sessionId)
                    .orElse(Cart.createAnonymous(sessionId));
        }

        throw new IllegalArgumentException("customerId ou sessionId obrigatório");
    }
}
