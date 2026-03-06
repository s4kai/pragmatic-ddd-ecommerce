package com.sakai.ecommerce.cart.domain;

import com.sakai.ecommerce.shared.domain.Money;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void shouldCreateCartItem() {
        var productId = UUID.randomUUID();
        var price = Money.of(100);

        var item = new CartItem(productId, "SKU-001", 2, price);

        assertNotNull(item.getId());
        assertEquals(productId, item.getProductId());
        assertEquals("SKU-001", item.getSku());
        assertEquals(2, item.getQuantity());
        assertEquals(price, item.getUnitPrice());
    }

    @Test
    void shouldThrowWhenCreatingWithZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CartItem(UUID.randomUUID(), "SKU-001", 0, Money.of(100)));
    }

    @Test
    void shouldThrowWhenCreatingWithNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CartItem(UUID.randomUUID(), "SKU-001", -1, Money.of(100)));
    }

    @Test
    void shouldThrowWhenCreatingWithNullPrice() {
        assertThrows(IllegalArgumentException.class, () -> 
            new CartItem(UUID.randomUUID(), "SKU-001", 1, null));
    }

    @Test
    void shouldIncreaseQuantity() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));

        item.increaseQuantity(3);

        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldThrowWhenIncreasingByZero() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        assertThrows(IllegalArgumentException.class, () -> item.increaseQuantity(0));
    }

    @Test
    void shouldThrowWhenIncreasingByNegative() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        assertThrows(IllegalArgumentException.class, () -> item.increaseQuantity(-1));
    }

    @Test
    void shouldDecreaseQuantity() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        item.decreaseQuantity(2);

        assertEquals(3, item.getQuantity());
    }

    @Test
    void shouldThrowWhenDecreasingByZero() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        assertThrows(IllegalArgumentException.class, () -> item.decreaseQuantity(0));
    }

    @Test
    void shouldThrowWhenDecreasingByNegative() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 5, Money.of(100));

        assertThrows(IllegalArgumentException.class, () -> item.decreaseQuantity(-1));
    }

    @Test
    void shouldNotGoNegativeWhenDecreasing() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 2, Money.of(100));

        item.decreaseQuantity(5);

        assertEquals(0, item.getQuantity());
    }

    @Test
    void shouldCalculateSubtotal() {
        var item = new CartItem(UUID.randomUUID(), "SKU-001", 3, Money.of(100));

        assertEquals(Money.of(300), item.getSubtotal());
    }












}
