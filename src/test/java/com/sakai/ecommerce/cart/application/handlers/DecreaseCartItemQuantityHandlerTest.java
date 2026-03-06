package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.DecreaseItemQuantityCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
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
class DecreaseCartItemQuantityHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartRetriever cartRetriever;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private DecreaseCartItemQuantityHandler handler;

    @Test
    void shouldDecreaseItemQuantity() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));
        var command = new DecreaseItemQuantityCommand("SKU-001", 2);

        when(cartRetriever.getCart()).thenReturn(cart);

        handler.decreaseQuantity(command);

        verify(cartRepository).save(cart);
        verify(eventPublisher).publish(cart);
    }

    @Test
    void shouldRemoveItemWhenQuantityReachesZero() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        var command = new DecreaseItemQuantityCommand("SKU-001", 2);

        when(cartRetriever.getCart()).thenReturn(cart);

        handler.decreaseQuantity(command);

        verify(cartRepository).save(cart);
        verify(eventPublisher).publish(cart);
        assertTrue(cart.getItems().isEmpty());
    }
}
