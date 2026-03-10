package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.AuthenticateCommand;
import com.sakai.ecommerce.auth.application.dto.AuthResponse;
import com.sakai.ecommerce.auth.application.services.PasswordEncoder;
import com.sakai.ecommerce.auth.application.services.TokenService;
import com.sakai.ecommerce.auth.domain.User;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.InvalidCredentials;
import com.sakai.ecommerce.shared.application.security.AuthorizationContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticateHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthenticateHandler.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public AuthResponse handle(AuthenticateCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(InvalidCredentials::new);

        log.debug("Trying login with {} email", command.email());

        boolean isPasswordValid = passwordEncoder.matches(
            command.password(),
            user.getPassword()
        );

        boolean validLogin = user.loginAttempt(isPasswordValid);

        if (!validLogin) {
            user.recordFailedLogin();
            userRepository.save(user);
            throw new InvalidCredentials();
        }

        String accessToken = tokenService.generateAccessToken(
            user.getId(),
            user.getEmail(),
            user.getRoles()
        );
        String refreshToken = tokenService.generateRefreshToken(user.getId());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new AuthResponse(accessToken, refreshToken, tokenService.getAccessTokenExpiresIn());
    }
}
