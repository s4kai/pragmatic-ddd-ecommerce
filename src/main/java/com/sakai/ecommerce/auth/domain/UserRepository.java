package com.sakai.ecommerce.auth.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailToken(String emailToken);
    User save(User user);
    void delete(User user);
}
