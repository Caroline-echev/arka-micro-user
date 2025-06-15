package com.arka.micro_user.adapters.driven.security.adapter;

import com.arka.micro_user.domain.spi.IJwtPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static com.arka.micro_user.adapters.driven.security.adapter.util.constants.SecurityAdapterConstants.*;

@Slf4j
@Component
public class JwtAdapter implements IJwtPersistencePort {

    private final SecretKey secretKey;
    private final Long expirationTime;

    public JwtAdapter(
            @Value("${jwt.secret}") String base64Key,
            @Value("${jwt.expiration-time}") Long expirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
        this.expirationTime = expirationTime;
    }

    @Override
    public Mono<String> generateToken(String userId, String email, String role) {
        log.info("Generating JWT token for userId: {}, email: {}, role: {}", userId, email, role);
        return Mono.fromCallable(() -> {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime * 1000);

            String token = Jwts.builder()
                    .setSubject(userId)
                    .claim(NAME_CLAIM_EMAIL, email)
                    .claim(NAME_CLAIM_ROLE, role)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();

            log.debug("Generated token: {}", token);
            return token;
        }).doOnError(e -> log.error("Error generating JWT token: {}", e.getMessage()));
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        log.info("Validating JWT token");
        return Mono.fromCallable(() -> {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            log.info("Token is valid");
            return true;
        }).onErrorResume(e -> {
            log.error("Invalid JWT token: {}", e.getMessage());
            return Mono.just(false);
        });
    }


    @Override
    public Mono<String> getUserIdFromToken(String token) {
        log.debug("Extracting userId from token");
        return Mono.fromCallable(() -> {
            String userId = getClaims(token).getSubject();
            log.info("Extracted userId: {}", userId);
            return userId;
        }).doOnError(e -> log.error("Error extracting userId: {}", e.getMessage()));
    }

    @Override
    public Mono<String> getEmailFromToken(String token) {
        log.debug("Extracting email from token");
        return Mono.fromCallable(() -> {
            String email = getClaims(token).get(NAME_CLAIM_EMAIL, String.class);
            log.info("Extracted email: {}", email);
            return email;
        }).doOnError(e -> log.error("Error extracting email: {}", e.getMessage()));
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        log.debug("Extracting role from token");
        return Mono.fromCallable(() -> {
            String role = getClaims(token).get(NAME_CLAIM_ROLE, String.class);
            log.info("Extracted role: {}", role);
            return role;
        }).doOnError(e -> log.error("Error extracting role: {}", e.getMessage()));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        log.info("Refreshing JWT token");
        return validateToken(token)
                .flatMap(isValid -> {
                    if (!isValid) {
                        log.warn("Cannot refresh token: token is invalid");
                        return Mono.error(new JwtException(TOKEN_INVALID));
                    }

                    return getUserIdFromToken(token)
                            .zipWith(getEmailFromToken(token))
                            .zipWith(getRoleFromToken(token))
                            .flatMap(tuple -> {
                                String userId = tuple.getT1().getT1();
                                String email = tuple.getT1().getT2();
                                String role = tuple.getT2();
                                log.info("Generating refreshed token for userId: {}", userId);
                                return generateToken(userId, email, role);
                            });
                }).doOnError(e -> log.error("Error refreshing token: {}", e.getMessage()));
    }

    @Override
    public Long getExpirationTime() {
        log.debug("Retrieving token expiration time: {} seconds", expirationTime);
        return expirationTime;
    }

    private Claims getClaims(String token) {
        log.debug("Parsing claims from token");
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
