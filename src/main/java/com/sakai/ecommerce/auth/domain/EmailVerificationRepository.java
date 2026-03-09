package com.sakai.ecommerce.auth.domain;

import java.util.Optional;
import java.util.UUID;

public interface EmailVerificationRepository {
    Optional<EmailVerification> findByToken(String token);
    Optional<EmailVerification> findByUserId(UUID userId);
    EmailVerification save(EmailVerification verification);
    void delete(EmailVerification verification);
}
