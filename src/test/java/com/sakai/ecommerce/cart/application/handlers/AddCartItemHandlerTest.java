package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.AddItemToCartCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.catalog.ProductPricingService;
import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCartItemHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductPricingService productPricingService;

    @Mock
    private CartRetriever cartRetriever;

    @InjectMocks
    private AddCartItemHandler handler;

    @Test
    void shouldAddItemToCart() {
        var customerId = UUID.randomUUID();
        var productId = UUID.randomUUID();
        var command = new AddItemToCartCommand(customerId, null, productId, "SKU-001", 2);
        var cart = new Cart(customerId);
        var price = Money.of(100);

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);
        when(productPricingService.getPrice(productId, "SKU-001")).thenReturn(price);

        handler.addItem(command);

        verify(cartRepository).save(cart);
    }

    @Test
    void shouldAddItemToAnonymousCart() {
        var sessionId = "session-123";
        var productId = UUID.randomUUID();
        var command = new AddItemToCartCommand(null, sessionId, productId, "SKU-001", 1);
        var cart = Cart.createAnonymous(sessionId);
        var price = Money.of(50);

        when(cartRetriever.getCart(null, sessionId)).thenReturn(cart);
        when(productPricingService.getPrice(productId, "SKU-001")).thenReturn(price);

        handler.addItem(command);

        verify(cartRepository).save(cart);
    }

    @Test
    void shouldAddMultipleItemsSequentially() {
        var customerId = UUID.randomUUID();
        var productId1 = UUID.randomUUID();
        var productId2 = UUID.randomUUID();
        var cart = new Cart(customerId);

        when(cartRetriever.getCart(customerId, null)).thenReturn(cart);
        when(productPricingService.getPrice(productId1, "SKU-001")).thenReturn(Money.of(100));
        when(productPricingService.getPrice(productId2, "SKU-002")).thenReturn(Money.of(200));

        handler.addItem(new AddItemToCartCommand(customerId, null, productId1, "SKU-001", 1));
        handler.addItem(new AddItemToCartCommand(customerId, null, productId2, "SKU-002", 2));

        verify(cartRepository, times(2)).save(cart);
    }
}
