package com.sakai.ecommerce.auth.infra.security;

import com.sakai.ecommerce.auth.application.services.TokenService;
import com.sakai.ecommerce.auth.domain.Role;
import com.sakai.ecommerce.auth.domain.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JwtTokenService implements TokenService {
    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public String generateAccessToken(UUID userId, String email, List<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("roles", roleNames)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public UUID validateAccessToken(String accessToken) {
        return extractUserId(accessToken);
    }

    @Override
    public UUID validateRefreshToken(String refreshToken) {
        return extractUserId(refreshToken);
    }

    @Override
    public List<String> extractRoles(String accessToken) {
        try {
            Claims claims = parseClaims(accessToken);
            return claims.get("roles", List.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    private UUID extractUserId(String token) {
        try {
            Claims claims = parseClaims(token);
            return UUID.fromString(claims.getSubject());
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired token");
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Long getAccessTokenExpiresIn() {
        return accessTokenExpiration;
    }
}
