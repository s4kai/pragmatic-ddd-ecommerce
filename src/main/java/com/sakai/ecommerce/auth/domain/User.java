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
    private final static int MAX_LOGIN_ATTEMPT = 5;
    private final static int EMAIL_VERIFICATION_TIME = 60 * 60; // 1h

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.PENDING_VERIFICATION;

    private boolean emailVerified = false;

    private String emailVerificationToken;
    private Instant emailVerificationTokenExpiry;

    private String passwordResetToken;
    private Instant passwordResetTokenExpiry;

    private int failedLoginAttempts = 0;

    private String refreshToken;
    private Instant lockedUntil;
    private Instant lastLoginAt;

    protected User() {}

    @Version
    private Long version;

    public static User createLocalUser(String email, String password){
        User.validateLocalUser(email, password);

        var user = new User();
        user.id = UUID.randomUUID();
        user.email = email;
        user.password = password;
        user.roles = List.of(Role.CUSTOMER);
        user.status = AccountStatus.PENDING_VERIFICATION;

        user.generateEmailVerificationToken();
        user.registerEvent(new UserRegisteredEvent(user.getId(), user.getEmail()));

        return user;
    }


    private static void validateLocalUser(String email, String password) {
        if (email == null || email.isBlank()) throw new BusinessError("Email cannot be null or blank");
        if (password == null || password.isBlank()) throw new BusinessError("Password cannot be null or blank");
    }

    public void generateEmailVerificationToken() {
        String verificationToken = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(EMAIL_VERIFICATION_TIME);

        this.emailVerificationToken = verificationToken;
        this.emailVerificationTokenExpiry = expiry;
    }

    public void verifyEmail(String token) {
        if (!token.equals(this.emailVerificationToken))
            throw new BusinessError("Invalid token");

        if (Instant.now().isAfter(this.emailVerificationTokenExpiry))
            throw new BusinessError("Token expired");

        this.emailVerified = true;
        this.status = AccountStatus.ACTIVE;
        this.emailVerificationToken = null;
        this.emailVerificationTokenExpiry = null;
    }

    public boolean loginAttempt(String hashPassword) {
        if (this.isAccountLocked()) return false;
        if (this.status != AccountStatus.ACTIVE) return false;

        if(hashPassword != null && !this.password.equals(hashPassword)) return false;

        this.recordSuccessfulLogin();
        this.registerEvent(new UserAuthenticatedEvent(this.getId(), this.getEmail()));
        return true;
    }

    public void revokeAllTokens(){
        this.refreshToken = null;
        this.emailVerificationToken = null;
        this.passwordResetToken = null;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= MAX_LOGIN_ATTEMPT) lockAccount();
    }

    private void lockAccount(){
        this.status = AccountStatus.LOCKED;
        this.lockedUntil = Instant.now().plusSeconds(900); // 15 min

    }

    public void resetPassword(String newPassword) {
        this.password = newPassword;
        this.passwordResetToken = null;
        this.passwordResetTokenExpiry = null;
    }

    private void recordSuccessfulLogin() {
        this.failedLoginAttempts = 0;
        this.lastLoginAt = Instant.now();
        this.lockedUntil = null;
    }

    public void logout(){
        this.revokeAllTokens();
    }

    public void delete(){
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
