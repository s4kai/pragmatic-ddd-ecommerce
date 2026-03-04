package com.sakai.ecommerce.shared.domain;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Money {
    private BigDecimal amount;
    private String currency;

    public Money(BigDecimal amount, Currency currency) {
        validateAmount(amount);
        validateCurrency(currency.getCurrencyCode());

        this.amount = amount;
        this.currency = currency.getCurrencyCode();
    }

    public Money(BigDecimal amount, String currencyCode) {
        validateAmount(amount);
        validateCurrency(currencyCode);

        this.amount = amount;
        this.currency = currencyCode;
    }

    public Money(BigDecimal amount) {
        this(amount, "BRL");
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Amount cannot be negative");
    }

    private void validateCurrency(String currencyCode) {
        if (currencyCode == null || currencyCode.isBlank()) throw new IllegalArgumentException("Currency cannot be null or empty");
        Currency.getInstance(currencyCode);
    }
}
