package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutHandler {
    private final CartRepository cartRepository;
    private final EventPublisher eventPublisher;
    private final AuthenticationContext authenticationContext;

    public void handle() {
       Cart cart = cartRepository.findByCustomerId(authenticationContext.getCurrentUserId())
                .orElseThrow(() -> new BusinessError("Cart not found"));

        cart.initiateCheckout();
        cartRepository.save(cart);
        
        eventPublisher.publish(cart);
    }

}
