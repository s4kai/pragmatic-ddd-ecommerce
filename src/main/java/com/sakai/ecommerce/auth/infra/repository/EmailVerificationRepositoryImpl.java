package com.sakai.ecommerce.auth.infra.repository;

import com.sakai.ecommerce.auth.domain.EmailVerification;
import com.sakai.ecommerce.auth.domain.EmailVerificationRepository;
import com.sakai.ecommerce.auth.infra.repository.jpa.JpaEmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {
    private final JpaEmailVerificationRepository jpaRepository;

    @Override
    public Optional<EmailVerification> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public Optional<EmailVerification> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public EmailVerification save(EmailVerification verification) {
        return jpaRepository.save(verification);
    }

    @Override
    public void delete(EmailVerification verification) {
        jpaRepository.delete(verification);
    }
}
