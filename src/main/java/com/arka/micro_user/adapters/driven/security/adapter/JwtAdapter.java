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
        return Mono.fromCallable(() -> {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationTime * 1000);

            return Jwts.builder()
                    .setSubject(userId)
                    .claim(NAME_CLAIM_EMAIL, email)
                    .claim(NAME_CLAIM_ROLE, role)
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
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_EMAIL, String.class));
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        return Mono.fromCallable(() -> getClaims(token).get(NAME_CLAIM_ROLE, String.class));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return validateToken(token)
                .flatMap(isValid -> {
                    if (!isValid) {
                        return Mono.error(new JwtException(TOKEN_INVALID));
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
