package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.auth.domain.Role;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SpringAuthenticationContext implements AuthenticationContext {

    @Override
    public UUID getCurrentUserId() {
        var principal = getAuthentication().getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        throw new IllegalStateException("Usuário não autenticado ou principal inválido");
    }

    @Override
    public String getCurrentUserEmail() {
        throw new UnsupportedOperationException("Email não disponível no contexto de autenticação");
    }

    @Override
    public Set<Role> getCurrentUserRoles() {
        return getAuthentication().getAuthorities().stream()
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
    public boolean isAuthenticated() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String);
    }

    private Authentication getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return auth;
    }

    @Override
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}