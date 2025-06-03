package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.domain.spi.IJwtPersistencePort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtAdapter implements IJwtPersistencePort {

    private final SecretKey secretKey;
    private final Long expirationTime = 3600L;

    public JwtAdapter() {
        String base64Key = "buP42/1FeXMwHJBrL6bfAeHZG9y0SAzGowCVciVc7YxwMyra92GB7XSfW3tcb3CKSg07IFXQqFw7+vh/bzBPNQ==";
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Key));
    }

    @Override
    public Mono<String> generateToken(String userId, String email, String role) {
        return Mono.fromCallable(() -> {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime * 1000);

            return Jwts.builder()
                    .setSubject(userId)
                    .claim("email", email)
                    .claim("role", role)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
        });
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                return false;
            }
        });
    }

    @Override
    public Mono<String> getUserIdFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).getSubject());
    }

    @Override
    public Mono<String> getEmailFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).get("email", String.class));
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).get("role", String.class));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return validateToken(token)
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new JwtException("Token inválido"));
                    }

                    return getUserIdFromToken(token)
                            .zipWith(getEmailFromToken(token))
                            .zipWith(getRoleFromToken(token))
                            .flatMap(tuple -> {
                                String userId = tuple.getT1().getT1();
                                String email = tuple.getT1().getT2();
                                String role = tuple.getT2();

                                return generateToken(userId, email, role);
                            });
                });
    }

    @Override
    public Long getExpirationTime() {
        return expirationTime;
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
