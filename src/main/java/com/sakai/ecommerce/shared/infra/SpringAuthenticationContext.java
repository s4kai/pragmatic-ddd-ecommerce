package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.auth.domain.Role;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import com.sakai.ecommerce.shared.application.security.AuthorizationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SpringAuthenticationContext implements AuthenticationContext, AuthorizationContext {

    @Override
    public Optional<UUID> getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        var principal = auth.getPrincipal();
        if (principal instanceof UUID uuid) {
            return Optional.of(uuid);
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Optional.empty();
        }
        var email = (String) auth.getDetails();
        return Optional.ofNullable(email);
    }

    @Override
    public Set<Role> getCurrentUserRoles() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return Set.of();
        }

        return auth.getAuthorities().stream()
                .map(authority -> {
                    String role = Objects.requireNonNull(authority.getAuthority()).replace("ROLE_", "");
                    return Role.valueOf(role);
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean hasRole(Role role) {
        return getCurrentUserRoles().contains(role);
    }

    @Override
    public boolean hasAnyRole(Role... roles) {
        var currentUserRoles = getCurrentUserRoles();
        for (Role role : roles) {
            if (currentUserRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String);
    }

    @Override
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}