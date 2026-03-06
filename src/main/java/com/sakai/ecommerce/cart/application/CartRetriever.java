package com.sakai.ecommerce.cart.application;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import com.sakai.ecommerce.shared.application.security.SessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartRetriever {
    private final CartRepository cartRepository;

    private final AuthenticationContext authenticationContext;
    private final SessionContext sessionContext;
    
    public Cart getCart() {
        var customerId = authenticationContext.getCurrentUserId();
        var sessionId = sessionContext.getCurrentSessionId();
        
        if (customerId != null) {
            return getAuthenticatedUserCart(customerId, sessionId);
        }

        return getAnonymousCart(sessionId);
    }

    private Cart getAuthenticatedUserCart(UUID customerId, String sessionId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> convertAnonymousCartOrCreateNew(customerId, sessionId));
    }

    private Cart convertAnonymousCartOrCreateNew(UUID customerId, String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .map(cart -> {
                    cart.assignToCustomer(customerId);
                    return cart;
                })
                .orElseGet(() -> new Cart(customerId));
    }

    private Cart getAnonymousCart(String sessionId) {
        if(sessionId == null || sessionId.isBlank())
            throw new RuntimeException("Erro ao processar carrinho");

        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> Cart.createAnonymous(sessionId));
    }
}
