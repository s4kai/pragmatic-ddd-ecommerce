package com.sakai.ecommerce.auth.domain;

import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class PasswordReset extends AggregateRoot<UUID> {
    private static final int EXPIRATION_TIME = 60 * 60; // 1h

    private UUID userId;
    private String token;
    private Instant expiresAt;
    private boolean used = false;

    protected PasswordReset() {}

    public static PasswordReset create(UUID userId) {
        var reset = new PasswordReset();
        reset.id = UUID.randomUUID();
        reset.userId = userId;
        reset.token = UUID.randomUUID().toString();
        reset.expiresAt = Instant.now().plusSeconds(EXPIRATION_TIME);
        return reset;
    }

    public void validate(String token) {
        if (!this.token.equals(token))
            throw new BusinessError("Invalid token");
        if (Instant.now().isAfter(expiresAt))
            throw new BusinessError("Token expired");
        if (used)
            throw new BusinessError("Token already used");
    }

    public void markAsUsed() {
        this.used = true;
    }
}
