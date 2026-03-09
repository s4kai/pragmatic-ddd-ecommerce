package com.sakai.ecommerce.auth.infra.repository.jpa;

import com.sakai.ecommerce.auth.domain.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaPasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
    Optional<PasswordReset> findByToken(String token);
    Optional<PasswordReset> findByUserId(UUID userId);
}
