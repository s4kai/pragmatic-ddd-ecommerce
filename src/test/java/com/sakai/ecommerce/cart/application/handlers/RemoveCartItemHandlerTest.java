package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.RemoveItemFromCartCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RemoveCartItemHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartRetriever cartRetriever;

    @InjectMocks
    private RemoveCartItemHandler handler;

    @Test
    void shouldRemoveItemFromCart() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        var command = new RemoveItemFromCartCommand(customerId, null, "SKU-001");

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);

        handler.removeItem(command);

        verify(cartRepository).save(cart);
    }

    @Test
    void shouldRemoveItemFromAnonymousCart() {
        var sessionId = "session-123";
        var cart = Cart.createAnonymous(sessionId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(50));
        var command = new RemoveItemFromCartCommand(null, sessionId, "SKU-001");

        when(cartRetriever.getCart(null, sessionId)).thenReturn(cart);

        handler.removeItem(command);

        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldHandleRemovingNonExistentItem() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        var command = new RemoveItemFromCartCommand(customerId, null, "SKU-999");

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);

        handler.removeItem(command);

        verify(cartRepository).save(cart);
        assertEquals(1, cart.getItems().size());
    }
}
