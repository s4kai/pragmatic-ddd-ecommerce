package com.sakai.ecommerce.auth.infra.repository.jpa;

import com.sakai.ecommerce.auth.domain.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaEmailVerificationRepository extends JpaRepository<EmailVerification, UUID> {
    Optional<EmailVerification> findByToken(String token);
    Optional<EmailVerification> findByUserId(UUID userId);
}
