package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.RemoveItemFromCartCommand;
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

@ExtendWith(MockitoExtension.class)
class RemoveCartItemHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartRetriever cartRetriever;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private RemoveCartItemHandler handler;

    @Test
    void shouldRemoveItemFromCart() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        var command = new RemoveItemFromCartCommand("SKU-001");

        when(cartRetriever.getCart()).thenReturn(cart);

        handler.removeItem(command);

        verify(cartRepository).save(cart);
        verify(eventPublisher).publish(cart);
    }
}
