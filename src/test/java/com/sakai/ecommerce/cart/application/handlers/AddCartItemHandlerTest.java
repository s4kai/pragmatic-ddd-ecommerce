package com.sakai.ecommerce.cart.application.handlers;

import com.sakai.ecommerce.cart.application.CartRetriever;
import com.sakai.ecommerce.cart.application.commands.AddItemToCartCommand;
import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.catalog.ProductPricingService;
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
class AddCartItemHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductPricingService productPricingService;

    @Mock
    private CartRetriever cartRetriever;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private AddCartItemHandler handler;

    @Test
    void shouldAddItemToCart() {
        var productId = UUID.randomUUID();
        var command = new AddItemToCartCommand(productId, "SKU-001", 2);
        var cart = new Cart(UUID.randomUUID());
        var price = Money.of(100);

        when(cartRetriever.getCart()).thenReturn(cart);
        when(productPricingService.getPrice(productId, "SKU-001")).thenReturn(price);

        handler.addItem(command);

        verify(cartRepository).save(cart);
        verify(eventPublisher).publish(cart);
    }
}
