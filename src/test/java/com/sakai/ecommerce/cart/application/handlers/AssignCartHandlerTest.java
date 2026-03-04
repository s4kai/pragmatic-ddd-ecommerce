package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.commands.AssignCartCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignCartHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private AssignCartHandler handler;

    @Test
    void shouldAssignCartToCustomer() {
        var sessionId = "session-123";
        var customerId = UUID.randomUUID();
        var cart = Cart.createAnonymous(sessionId);
        var command = new AssignCartCommand(customerId, sessionId);

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        handler.assign(command);

        verify(cartRepository).save(cart);
    }

    @Test
    void shouldThrowWhenCartNotFound() {
        var command = new AssignCartCommand(UUID.randomUUID(),"session-123");

        when(cartRepository.findBySessionId("session-123")).thenReturn(Optional.empty());

        assertThrows(BusinessError.class, () -> handler.assign(command));
    }

    @Test
    void shouldReassignCartToDifferentCustomer() {
        var sessionId = "session-123";
        var customerId1 = UUID.randomUUID();
        var customerId2 = UUID.randomUUID();
        var cart = Cart.createAnonymous(sessionId);
        cart.assignToCustomer(customerId1);

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        handler.assign(new AssignCartCommand(customerId2, sessionId));

        verify(cartRepository).save(cart);
        assertEquals(customerId2, cart.getCustomerId());
    }

    @Test
    void shouldAssignCartWithItems() {
        var sessionId = "session-123";
        var customerId = UUID.randomUUID();
        var cart = Cart.createAnonymous(sessionId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        var command = new AssignCartCommand(customerId, sessionId);

        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        handler.assign(command);

        verify(cartRepository).save(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals(customerId, cart.getCustomerId());
    }
}
