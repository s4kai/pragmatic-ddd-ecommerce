package com.sakai.ecommerce.customer.domain.events;

import com.sakai.ecommerce.shared.domain.core.DomainEvent;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class CreatedCustomerEvent extends DomainEvent {
    private UUID customerId;
}
