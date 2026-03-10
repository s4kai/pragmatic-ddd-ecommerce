package com.sakai.ecommerce.shared.application.security;

import com.sakai.ecommerce.auth.domain.Role;

import java.util.Set;

public interface AuthorizationContext {
    Set<Role> getCurrentUserRoles();
    boolean hasAnyRole(Role... roles);
    boolean hasRole(Role role);
}
