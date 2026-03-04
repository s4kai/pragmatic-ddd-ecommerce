package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.commands.AssignCartCommand;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignCartHandler {
    private final CartRepository cartRepository;

    @Transactional
    public void assign(AssignCartCommand command) {
        var cart = cartRepository.findBySessionId(command.sessionId())
                .orElseThrow(() -> new BusinessError("Cart not found"));

        cart.assignToCustomer(command.customerId());
        cartRepository.save(cart);
    }
}
