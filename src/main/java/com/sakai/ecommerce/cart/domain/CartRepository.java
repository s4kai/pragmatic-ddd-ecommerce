package com.sakai.ecommerce.cart.domain;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    Optional<Cart> findByCustomerId(UUID customerId);
    Optional<Cart> findBySessionId(String sessionId);
    Cart save(Cart cart);
}
