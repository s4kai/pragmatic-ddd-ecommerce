package com.sakai.ecommerce.cart.domain;

import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void shouldCreateCartForCustomer() {
        var customerId = UUID.randomUUID();
        var cart = new Cart(customerId);

        assertNotNull(cart.getId());
        assertEquals(customerId, cart.getCustomerId());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());
        assertNull(cart.getSessionId());
    }

    @Test
    void shouldCreateAnonymousCart() {
        var sessionId = "session-123";
        var cart = Cart.createAnonymous(sessionId);

        assertNotNull(cart.getId());
        assertEquals(sessionId, cart.getSessionId());
        assertNull(cart.getCustomerId());
    }

    @Test
    void shouldAssignCartToCustomer() {
        var cart = Cart.createAnonymous("session-123");
        var customerId = UUID.randomUUID();

        cart.assignToCustomer(customerId);

        assertEquals(customerId, cart.getCustomerId());
        assertNull(cart.getSessionId());
    }

    @Test
    void shouldAddItemToCart() {
        var cart = new Cart(UUID.randomUUID());
        var productId = UUID.randomUUID();
        var price = Money.of(100);

        cart.addItem(productId, "SKU-001", 2, price);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingExistingItem() {
        var cart = new Cart(UUID.randomUUID());
        var productId = UUID.randomUUID();
        var price = Money.of(100);

        cart.addItem(productId, "SKU-001", 2, price);
        cart.addItem(productId, "SKU-001", 3, price);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void shouldRemoveItemFromCart() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));

        cart.removeItem("SKU-001");

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldDecreaseItemQuantity() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        cart.decreaseItemQuantity("SKU-001", 2);

        assertEquals(3, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void shouldRemoveItemWhenQuantityReachesZero() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));

        cart.decreaseItemQuantity("SKU-001", 2);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldCalculateTotal() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));
        cart.addItem(UUID.randomUUID(), "SKU-002", 1, Money.of(50));

        assertEquals(Money.of(250), cart.getTotal());
    }

    @Test
    void shouldInitiateCheckout() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));

        cart.initiateCheckout();

        assertEquals(CartStatus.CHECKED_OUT, cart.getStatus());
        assertEquals(1, cart.getDomainEvents().size());
    }

    @Test
    void shouldThrowWhenCheckoutEmptyCart() {
        var cart = new Cart(UUID.randomUUID());

        assertThrows(BusinessError.class, cart::initiateCheckout);
    }

    @Test
    void shouldThrowWhenCheckoutAnonymousCart() {
        var cart = Cart.createAnonymous("session-123");
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));

        assertThrows(BusinessError.class, cart::initiateCheckout);
    }

    @Test
    void shouldThrowWhenCheckoutAlreadyCheckedOutCart() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));
        cart.initiateCheckout();

        assertThrows(BusinessError.class, cart::initiateCheckout);
    }

    @Test
    void shouldReturnZeroTotalForEmptyCart() {
        var cart = new Cart(UUID.randomUUID());

        assertEquals(Money.ZERO, cart.getTotal());
    }

    @Test
    void shouldThrowWhenDecreasingNonExistentItem() {
        var cart = new Cart(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> cart.decreaseItemQuantity("SKU-999", 1));
    }

    @Test
    void shouldHandleMultipleDifferentItems() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));
        cart.addItem(UUID.randomUUID(), "SKU-002", 2, Money.of(50));
        cart.addItem(UUID.randomUUID(), "SKU-003", 3, Money.of(25));

        assertEquals(3, cart.getItems().size());
        assertEquals(Money.of(275), cart.getTotal());
    }

    @Test
    void shouldAddZeroQuantityItem() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 0, Money.of(100));

        assertEquals(1, cart.getItems().size());
        assertEquals(0, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void shouldRemoveNonExistentItemWithoutError() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 1, Money.of(100));

        cart.removeItem("SKU-999");

        assertEquals(1, cart.getItems().size());
    }

    @Test
    void shouldDecreaseByMoreThanAvailable() {
        var cart = new Cart(UUID.randomUUID());
        cart.addItem(UUID.randomUUID(), "SKU-001", 3, Money.of(100));

        cart.decreaseItemQuantity("SKU-001", 10);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void shouldAssignAnonymousCartMultipleTimes() {
        var cart = Cart.createAnonymous("session-123");
        var customerId1 = UUID.randomUUID();
        var customerId2 = UUID.randomUUID();

        cart.assignToCustomer(customerId1);
        cart.assignToCustomer(customerId2);

        assertEquals(customerId2, cart.getCustomerId());
        assertNull(cart.getSessionId());
    }
}
