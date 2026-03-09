package com.sakai.ecommerce.auth.application.handlers;

import com.sakai.ecommerce.auth.application.commands.RefreshTokenCommand;
import com.sakai.ecommerce.auth.application.dto.AuthResponse;
import com.sakai.ecommerce.auth.application.services.TokenService;
import com.sakai.ecommerce.auth.domain.User;
import com.sakai.ecommerce.auth.domain.UserRepository;
import com.sakai.ecommerce.auth.domain.exceptions.InvalidCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse handle(RefreshTokenCommand command) {
        String sentRefreshToken = command.refreshToken();
        User user = retrieveUserAndValidateRefreshToken(sentRefreshToken);

        String accessToken = tokenService.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = tokenService.generateRefreshToken(user.getId());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new AuthResponse(accessToken, newRefreshToken, tokenService.getAccessTokenExpiresIn());
    }

    private User retrieveUserAndValidateRefreshToken(String sentRefreshToken) {
        UUID userId = tokenService.validateRefreshToken(sentRefreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentials::new);

        if (isRefreshTokenMismatch(user, sentRefreshToken)) {
            revokeUserTokens(user);
            throw new InvalidCredentials();
        }

        return user;
    }

    private boolean isRefreshTokenMismatch(User user, String sentRefreshToken) {
        return user == null || !user.getRefreshToken().equals(sentRefreshToken);
    }

    private void revokeUserTokens(User user) {
        user.revokeAllTokens();
        userRepository.save(user);
    }
}
