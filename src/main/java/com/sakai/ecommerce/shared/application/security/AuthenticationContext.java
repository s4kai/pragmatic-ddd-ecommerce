package com.sakai.ecommerce.shared.application.security;

import com.sakai.ecommerce.auth.domain.Role;

import java.util.Set;
import java.util.UUID;

public interface AuthenticationContext {
    UUID getCurrentUserId();
    String getCurrentUserEmail();
    Set<Role> getCurrentUserRoles();
    boolean hasRole(Role role);
    boolean isAuthenticated();
    void clearContext();
}
