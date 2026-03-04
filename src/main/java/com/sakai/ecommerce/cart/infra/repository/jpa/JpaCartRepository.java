package com.sakai.ecommerce.cart.infra.repository.jpa;

import com.sakai.ecommerce.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByCustomerId(UUID customerId);
    Optional<Cart> findBySessionId(String sessionId);
}
