package com.sakai.ecommerce.cart.domain;

import com.sakai.ecommerce.cart.domain.events.CartCheckoutInitiatedEvent;
import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.Money;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@NoArgsConstructor
@Getter
public class Cart extends AggregateRoot<UUID> {

    private UUID customerId;
    private String sessionId;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @JoinColumn(name = "cart_id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartItem> items = new ArrayList<>();

    public Cart(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.sessionId = null;
        this.status = CartStatus.ACTIVE;
    }

    public static Cart createAnonymous(String sessionId) {
        var cart = new Cart();
        cart.id = UUID.randomUUID();
        cart.sessionId = sessionId;
        return cart;
    }

    public void assignToCustomer(UUID customerId) {
        this.customerId = customerId;
        this.sessionId = null;
    }

    public void addItem(UUID productId, String sku, int quantity, Money unitPrice) {
        var existing = items.stream()
                .filter(i -> i.getSku().equals(sku))
                .findFirst();

        if (existing.isPresent()) {
            var existingItem = existing.get();
            existingItem.increaseQuantity(quantity);
        } else {
            var newItem = new CartItem(productId, sku, quantity, unitPrice);
            items.add(newItem);
        }
    }

    public void removeItem(String sku) {
        items.removeIf(i -> i.getSku().equals(sku));
    }
    
    public void decreaseItemQuantity(String sku, int quantity) {
        var item = items.stream()
                .filter(i -> i.getSku().equals(sku))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));
        
        item.decreaseQuantity(quantity);
        
        if (item.getQuantity() == 0) {
            items.remove(item);
        }
    }

    public void initiateCheckout() {
        if (this.status != CartStatus.ACTIVE) {
            throw new BusinessError("Não é possível iniciar o checkout de um carrinho que não está ativo");
        }

        if(this.items.isEmpty()) throw new BusinessError("Não é possível fazer checkout de um carrinho vazio");

        if (Objects.isNull(this.customerId)) {
            throw new BusinessError("Não é possível fazer checkout de um carrinho anônimo");
        }

        this.status = CartStatus.CHECKED_OUT;
        registerEvent(new CartCheckoutInitiatedEvent(this.id, this.customerId));
    }

    public Money getTotal() {
        if(items.isEmpty()) return Money.ZERO;

        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(Money.ZERO, Money::add);
    }
}
