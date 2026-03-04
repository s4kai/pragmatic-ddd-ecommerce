package com.sakai.ecommerce.cart.application;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
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
class CartRetrieverTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartRetriever cartRetriever;

    @Test
    void shouldGetExistingCartByCustomerId() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart(customerId, null);

        assertEquals(cart, result);
        verify(cartRepository).findByCustomerId(customerId);
    }

    @Test
    void shouldCreateNewCartWhenCustomerHasNoCart() {
        var customerId = UUID.randomUUID();
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        var result = cartRetriever.getCart(customerId, null);

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
    }

    @Test
    void shouldGetExistingCartBySessionId() {
        var sessionId = "session-123";
        var cart = Cart.createAnonymous(sessionId);
        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart(null, sessionId);

        assertEquals(cart, result);
        verify(cartRepository).findBySessionId(sessionId);
    }

    @Test
    void shouldCreateAnonymousCartWhenSessionHasNoCart() {
        var sessionId = "session-123";
        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        var result = cartRetriever.getCart(null, sessionId);

        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
    }

    @Test
    void shouldThrowWhenBothIdsAreNull() {
        assertThrows(IllegalArgumentException.class, () -> cartRetriever.getCart(null, null));
    }

    @Test
    void shouldPrioritizeCustomerIdOverSessionId() {
        var customerId = UUID.randomUUID();
        var sessionId = "session-123";
        var cart = new Cart(customerId);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart(customerId, sessionId);

        assertEquals(cart, result);
        verify(cartRepository).findByCustomerId(customerId);
        verify(cartRepository, never()).findBySessionId(anyString());
    }

    @Test
    void shouldHandleEmptySessionId() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart(customerId, "");

        assertEquals(cart, result);
    }
}
