package com.sakai.ecommerce.auth.infra.controllers;

import com.sakai.ecommerce.auth.application.commands.AuthenticateCommand;
import com.sakai.ecommerce.auth.application.commands.LogoutCommand;
import com.sakai.ecommerce.auth.application.commands.RefreshTokenCommand;
import com.sakai.ecommerce.auth.application.commands.RegisterUserCommand;
import com.sakai.ecommerce.auth.application.dto.AuthResponse;
import com.sakai.ecommerce.auth.application.handlers.AuthenticateHandler;
import com.sakai.ecommerce.auth.application.handlers.LogoutHandler;
import com.sakai.ecommerce.auth.application.handlers.RefreshTokenHandler;
import com.sakai.ecommerce.auth.application.handlers.RegisterUserHandler;
import com.sakai.ecommerce.auth.infra.requests.LoginRequest;
import com.sakai.ecommerce.auth.infra.requests.RefreshTokenRequest;
import com.sakai.ecommerce.auth.infra.requests.RegisterRequest;
import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RegisterUserHandler registerUserHandler;
    private final AuthenticateHandler authenticateHandler;
    private final RefreshTokenHandler refreshTokenHandler;
    private final LogoutHandler logoutHandler;
    private final AuthenticationContext authenticationContext;

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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        logoutHandler.handler(new LogoutCommand(authenticationContext.getCurrentUserId()));
        authenticationContext.clearContext();
        return ResponseEntity.noContent().build();
    }
}
