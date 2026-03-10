package com.sakai.ecommerce.auth.infra.controllers;

import com.sakai.ecommerce.auth.application.commands.*;
import com.sakai.ecommerce.auth.application.dto.AuthResponse;
import com.sakai.ecommerce.auth.application.handlers.*;
import com.sakai.ecommerce.auth.infra.requests.LoginRequest;
import com.sakai.ecommerce.auth.infra.requests.RefreshTokenRequest;
import com.sakai.ecommerce.auth.infra.requests.RegisterRequest;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.sakai.ecommerce.shared.infra.security.ApplicationRoles.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserHandler registerUserHandler;
    private final AuthenticateHandler authenticateHandler;
    private final RefreshTokenHandler refreshTokenHandler;
    private final LogoutHandler logoutHandler;
    private final AuthenticationContext authenticationContext;
    private final VerifyEmailHandler verifyEmailHandler;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        registerUserHandler.handle(new RegisterUserCommand(request.email(), request.password()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authenticateHandler.handle(new AuthenticateCommand(request.email(), request.password()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = refreshTokenHandler.handle(new RefreshTokenCommand(request.refreshToken()));
        return ResponseEntity.ok(response);
    }

    @Secured({ADMIN, CUSTOMER, MANAGER})
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        var userId = authenticationContext.getCurrentUserId()
                .orElse(null);
        var command = new LogoutCommand(userId);

        logoutHandler.handler(command);
        authenticationContext.clearContext();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/verification/{token}")
    public ResponseEntity<Void> verifyEmail (@PathVariable String token) {
        var command = new VerifyEmailCommand(token);
        verifyEmailHandler.handle(command);
        return ResponseEntity.ok().build();
    }
}
