package com.sakai.ecommerce.customer.domain.events;

import com.sakai.ecommerce.shared.core.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
public class CreatedCustomerEvent extends DomainEvent {
    private UUID customerId;
}
