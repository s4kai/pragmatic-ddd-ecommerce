package com.sakai.ecommerce.auth.domain.events;

import com.sakai.ecommerce.shared.domain.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserAuthenticatedEvent extends DomainEvent {
    private final UUID userId;
    private final String email;

    public UserAuthenticatedEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
