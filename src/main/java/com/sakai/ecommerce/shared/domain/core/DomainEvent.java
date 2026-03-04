package com.sakai.ecommerce.shared.domain.core;

import lombok.Data;
import java.util.UUID;

@Data
public class DomainEvent{
    private final UUID eventId = UUID.randomUUID();
}
