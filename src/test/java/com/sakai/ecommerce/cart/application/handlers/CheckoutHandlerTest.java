package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.commands.CheckoutCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CheckoutHandler handler;

    @Test
    void shouldCheckoutCart() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));
        var command = new CheckoutCommand(customerId);

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        handler.handle(command);

        verify(cartRepository).save(cart);
        verify(eventPublisher).publish(cart);
    }

    @Test
    void shouldThrowWhenCartNotFound() {
        var customerId = UUID.randomUUID();
        var command = new CheckoutCommand(customerId);

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(BusinessError.class, () -> handler.handle(command));
    }

    @Test
    void shouldNotPublishEventsWhenCheckoutFails() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        var command = new CheckoutCommand(customerId);

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        assertThrows(BusinessError.class, () -> handler.handle(command));
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void shouldThrowWhenCheckoutEmptyCart() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        var command = new CheckoutCommand(customerId);

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        assertThrows(BusinessError.class, () -> handler.handle(command));
    }
}
