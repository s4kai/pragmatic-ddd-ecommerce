package com.sakai.ecommerce.cart.domain.events;

import com.sakai.ecommerce.shared.domain.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CartCheckoutInitiatedEvent extends DomainEvent {
    private final UUID cartId;
    private final UUID customerId;

    public CartCheckoutInitiatedEvent(UUID cartId, UUID customerId) {
        this.cartId = cartId;
        this.customerId = customerId;
    }
}
