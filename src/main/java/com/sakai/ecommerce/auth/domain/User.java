package com.sakai.ecommerce.auth.domain;

import com.sakai.ecommerce.auth.domain.events.UserAuthenticatedEvent;
import com.sakai.ecommerce.auth.domain.events.UserRegisteredEvent;
import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class User extends AggregateRoot<UUID> {
    private static final int MAX_LOGIN_ATTEMPT = 5;

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.PENDING_VERIFICATION;

    private int failedLoginAttempts = 0;
    private String refreshToken;
    private Instant lockedUntil;
    private Instant lastLoginAt;

    protected User() {}

    @Version
    private Long version;

    public static User createLocalUser(String email, String password){
        validateLocalUser(email, password);

        var user = new User();
        user.id = UUID.randomUUID();
        user.email = email;
        user.password = password;
        user.roles = List.of(Role.CUSTOMER);
        user.status = AccountStatus.PENDING_VERIFICATION;
        user.registerEvent(new UserRegisteredEvent(user.getId(), user.getEmail()));

        return user;
    }


    private static void validateLocalUser(String email, String password) {
        if (email == null || email.isBlank()) throw new BusinessError("Email cannot be null or blank");
        if (password == null || password.isBlank()) throw new BusinessError("Password cannot be null or blank");
    }

    public void activateAccount() {
        this.status = AccountStatus.ACTIVE;
    }

    public boolean loginAttempt(boolean passwordIsValid) {
        if (isAccountLocked()) return false;
        if (status != AccountStatus.ACTIVE) return false;
        if (!passwordIsValid) return false;

        recordSuccessfulLogin();
        registerEvent(new UserAuthenticatedEvent(getId(), getEmail()));
        return true;
    }

    public void revokeRefreshToken() {
        this.refreshToken = null;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (failedLoginAttempts >= MAX_LOGIN_ATTEMPT) lockAccount();
    }

    private void lockAccount() {
        this.status = AccountStatus.LOCKED;
        this.lockedUntil = Instant.now().plusSeconds(900); // 15 min
    }

    public void updatePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank())
            throw new BusinessError("Password cannot be null or blank");
        this.password = newPassword;
    }

    private void recordSuccessfulLogin() {
        this.failedLoginAttempts = 0;
        this.lastLoginAt = Instant.now();
        this.lockedUntil = null;
    }

    public void logout() {
        revokeRefreshToken();
    }

    public void delete() {
        this.status = AccountStatus.TEMP_DELETED;
    }

    public boolean isAccountLocked() {
        if (lockedUntil != null && Instant.now().isBefore(lockedUntil)) {
            return true;
        }
        if (lockedUntil != null && Instant.now().isAfter(lockedUntil)) {
            this.status = AccountStatus.ACTIVE;
            this.lockedUntil = null;
        }
        return false;
    }

}
