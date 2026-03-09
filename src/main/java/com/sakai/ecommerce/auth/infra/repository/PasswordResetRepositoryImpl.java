package com.sakai.ecommerce.auth.infra.repository;

import com.sakai.ecommerce.auth.domain.PasswordReset;
import com.sakai.ecommerce.auth.domain.PasswordResetRepository;
import com.sakai.ecommerce.auth.infra.repository.jpa.JpaPasswordResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PasswordResetRepositoryImpl implements PasswordResetRepository {
    private final JpaPasswordResetRepository jpaRepository;

    @Override
    public Optional<PasswordReset> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public Optional<PasswordReset> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public PasswordReset save(PasswordReset reset) {
        return jpaRepository.save(reset);
    }

    @Override
    public void delete(PasswordReset reset) {
        jpaRepository.delete(reset);
    }
}
