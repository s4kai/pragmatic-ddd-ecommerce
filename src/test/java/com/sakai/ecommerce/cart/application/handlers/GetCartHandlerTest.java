package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.queries.GetCartQuery;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.catalog.ProductInfoService;
import com.sakai.ecommerce.shared.domain.Money;
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
class GetCartHandlerTest {

    @Mock
    private CartRetriever cartRetriever;

    @Mock
    private ProductInfoService productInfoService;

    @InjectMocks
    private GetCartHandler handler;

    @Test
    void shouldGetCart() {
        var customerId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        var cart = new Cart(customerId);
        cart.addItem(productId, "SKU-001", 2, Money.of(100));
        var query = new GetCartQuery(customerId, null);

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);
        when(productInfoService.getProductInfo(productId, "SKU-001")).thenReturn(Optional.empty());

        var response = handler.handle(query);

        assertNotNull(response);
        assertEquals(cart.getId(), response.id());
        assertEquals(1, response.items().size());
    }

    @Test
    void shouldGetEmptyCart() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        var query = new GetCartQuery(customerId, null);

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);

        var response = handler.handle(query);

        assertNotNull(response);
        assertTrue(response.items().isEmpty());
        assertEquals(Money.ZERO, response.total());
    }

    @Test
    void shouldGetAnonymousCart() {
        var sessionId = "session-123";
        var productId = UUID.randomUUID();
        var cart = Cart.createAnonymous(sessionId);
        cart.addItem(productId, "SKU-001", 1, Money.of(50));
        var query = new GetCartQuery(null, sessionId);

        when(cartRetriever.getCart(null, sessionId)).thenReturn(cart);
        when(productInfoService.getProductInfo(productId, "SKU-001")).thenReturn(Optional.empty());

        var response = handler.handle(query);

        assertNotNull(response);
        assertEquals(sessionId, response.sessionId());
        assertEquals(1, response.items().size());
    }

    @Test
    void shouldGetCartWithMultipleItems() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        cart.addItem(UUID.randomUUID(), "SKU-002", 1, Money.of(200));
        cart.addItem(UUID.randomUUID(), "SKU-003", 3, Money.of(50));
        var query = new GetCartQuery(customerId, null);

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);
        when(productInfoService.getProductInfo(any(), any())).thenReturn(Optional.empty());

        var response = handler.handle(query);

        assertEquals(3, response.items().size());
        assertEquals(Money.of(550), response.total());
    }
}
