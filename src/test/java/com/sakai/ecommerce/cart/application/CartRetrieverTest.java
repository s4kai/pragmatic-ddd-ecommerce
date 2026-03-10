package com.sakai.ecommerce.cart.application;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import com.sakai.ecommerce.shared.application.security.SessionContext;
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

    @Mock
    private AuthenticationContext authenticationContext;

    @Mock
    private SessionContext sessionContext;

    @InjectMocks
    private CartRetriever cartRetriever;

    @Test
    void shouldGetExistingCartByCustomerId() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        
        when(authenticationContext.getCurrentUserId()).thenReturn(Optional.of(customerId));
        when(sessionContext.getCurrentSessionId()).thenReturn("session");
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart();

        assertEquals(cart, result);
    }

    @Test
    void shouldCreateNewCartWhenCustomerHasNoCart() {
        var customerId = UUID.randomUUID();
        var sessionId = "session-123";
        
        when(authenticationContext.getCurrentUserId()).thenReturn(Optional.of(customerId));
        when(sessionContext.getCurrentSessionId()).thenReturn(sessionId);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        var result = cartRetriever.getCart();

        assertNotNull(result);
        assertEquals(customerId, result.getCustomerId());
    }

    @Test
    void shouldGetExistingCartBySessionId() {
        var sessionId = "session-123";
        var cart = Cart.createAnonymous(sessionId);
        
        when(authenticationContext.getCurrentUserId()).thenReturn(null);
        when(sessionContext.getCurrentSessionId()).thenReturn(sessionId);
        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(cart));

        var result = cartRetriever.getCart();

        assertEquals(cart, result);
    }

    @Test
    void shouldConvertAnonymousCartToAuthenticatedCart() {
        var customerId = UUID.randomUUID();
        var sessionId = "session-123";
        var anonymousCart = Cart.createAnonymous(sessionId);
        
        when(authenticationContext.getCurrentUserId()).thenReturn(Optional.of(customerId));
        when(sessionContext.getCurrentSessionId()).thenReturn(sessionId);
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(cartRepository.findBySessionId(sessionId)).thenReturn(Optional.of(anonymousCart));

        var result = cartRetriever.getCart();

        assertEquals(customerId, result.getCustomerId());
        assertNull(result.getSessionId());
    }
}
