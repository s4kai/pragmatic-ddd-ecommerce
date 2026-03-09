package com.sakai.ecommerce.auth.domain;

import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
public class EmailVerification extends AggregateRoot<UUID> {
    private static final int EXPIRATION_TIME = 60 * 60; // 1h

    private UUID userId;
    private String token;
    private Instant expiresAt;
    private boolean verified = false;

    protected EmailVerification() {}

    public static EmailVerification create(UUID userId) {
        var verification = new EmailVerification();
        verification.id = UUID.randomUUID();
        verification.userId = userId;
        verification.token = UUID.randomUUID().toString();
        verification.expiresAt = Instant.now().plusSeconds(EXPIRATION_TIME);
        return verification;
    }

    public void verify(String token) {
        if (!this.token.equals(token))
            throw new BusinessError("Invalid token");
        if (Instant.now().isAfter(expiresAt))
            throw new BusinessError("Token expired");
        if (verified)
            throw new BusinessError("Already verified");

        this.verified = true;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
