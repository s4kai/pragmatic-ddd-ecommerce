package com.sakai.ecommerce.auth.domain;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepository {
    Optional<PasswordReset> findByToken(String token);
    Optional<PasswordReset> findByUserId(UUID userId);
    PasswordReset save(PasswordReset reset);
    void delete(PasswordReset reset);
}
