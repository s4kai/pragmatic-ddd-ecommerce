package com.sakai.ecommerce.cart.infra.repository;

import com.sakai.ecommerce.cart.domain.Cart;
import com.sakai.ecommerce.cart.domain.CartRepository;
import com.sakai.ecommerce.cart.infra.repository.jpa.JpaCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {
    private final JpaCartRepository jpaRepository;

    @Override
    public Optional<Cart> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Cart> findBySessionId(String sessionId) {
        return jpaRepository.findBySessionId(sessionId);
    }

    @Override
    public Cart save(Cart cart) {
        return jpaRepository.save(cart);
    }
}
