package com.sakai.ecommerce.auth.domain.events;

import com.sakai.ecommerce.shared.domain.core.DomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserRegisteredEvent extends DomainEvent {
    private final UUID userId;
    private final String email;

    public UserRegisteredEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
