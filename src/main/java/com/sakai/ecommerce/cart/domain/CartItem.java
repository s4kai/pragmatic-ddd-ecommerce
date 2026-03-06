package com.sakai.ecommerce.cart.domain;

import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.core.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class CartItem extends BaseEntity<UUID> {

    private UUID productId;
    private String sku;
    private int quantity;

    @Embedded
    private Money unitPrice;

    public CartItem(UUID productId, String sku, int quantity, Money unitPrice) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        if (unitPrice == null || unitPrice.isNegative()) throw new IllegalArgumentException("Preço inválido");
        
        this.id = UUID.randomUUID();
        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        this.quantity = Math.max(0, this.quantity - amount);
    }

    public Money getSubtotal() {
        return unitPrice.multiply(quantity);
    }
}

