package com.sakai.ecommerce.shared.application.security;

import java.util.Optional;
import java.util.UUID;

public interface AuthenticationContext {
    Optional<UUID> getCurrentUserId();
    Optional<String> getCurrentUserEmail();
    boolean isAuthenticated();
    void clearContext();
}
