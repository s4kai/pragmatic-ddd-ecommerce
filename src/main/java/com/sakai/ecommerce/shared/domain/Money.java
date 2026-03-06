package com.sakai.ecommerce.shared.domain;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;
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

    public static Money ZERO = new Money(BigDecimal.ZERO);

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

    public Money multiply(int multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }

    public Money add(Money other){
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(amount.multiply(multiplier), currency);
    }
    
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public static Money of(Integer amount){
        try{
            return new Money(BigDecimal.valueOf(amount));
        }catch (Exception e){
            throw new BusinessError("Invalid amount: " + amount, e);
        }
    }

    public static Money of(BigDecimal amount, Currency currency){
        return new Money(amount, currency);
    }
}
