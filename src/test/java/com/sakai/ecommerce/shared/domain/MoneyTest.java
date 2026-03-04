package com.sakai.ecommerce.shared.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoney() {
        var money = new Money(BigDecimal.valueOf(100), "BRL");

        assertEquals(BigDecimal.valueOf(100), money.getAmount());
        assertEquals("BRL", money.getCurrency());
    }

    @Test
    void shouldCreateMoneyWithDefaultCurrency() {
        var money = new Money(BigDecimal.valueOf(100));

        assertEquals("BRL", money.getCurrency());
    }

    @Test
    void shouldCreateMoneyFromInteger() {
        var money = Money.of(100);

        assertEquals(BigDecimal.valueOf(100), money.getAmount());
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null, "BRL"));
    }

    @Test
    void shouldThrowWhenAmountIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.valueOf(-10), "BRL"));
    }

    @Test
    void shouldThrowWhenCurrencyIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Money(BigDecimal.TEN, "INVALID"));
    }

    @Test
    void shouldMultiplyByInteger() {
        var money = Money.of(100);

        var result = money.multiply(3);

        assertEquals(BigDecimal.valueOf(300), result.getAmount());
    }

    @Test
    void shouldMultiplyByBigDecimal() {
        var money = Money.of(100);

        var result = money.multiply(BigDecimal.valueOf(2.5));

        assertEquals(BigDecimal.valueOf(250.0), result.getAmount());
    }

    @Test
    void shouldAddMoney() {
        var money1 = Money.of(100);
        var money2 = Money.of(50);

        var result = money1.add(money2);

        assertEquals(BigDecimal.valueOf(150), result.getAmount());
    }

    @Test
    void shouldHaveZeroConstant() {
        assertEquals(BigDecimal.ZERO, Money.ZERO.getAmount());
    }

    @Test
    void shouldBeEqualWhenSameAmountAndCurrency() {
        var money1 = Money.of(100);
        var money2 = Money.of(100);

        assertEquals(money1, money2);
    }

    @Test
    void shouldNotBeEqualWhenDifferentAmount() {
        var money1 = Money.of(100);
        var money2 = Money.of(200);

        assertNotEquals(money1, money2);
    }
}
