package com.arka.micro_user.configuration.security;

import com.arka.micro_user.domain.spi.IJwtPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final IJwtPersistencePort jwtPersistencePort;

    // Rutas que no requieren autenticación JWT
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/users/client",
            "/api/users/change-password",
            "/swagger-ui",
            "/v3/api-docs",
            "/api-docs",
            "/webjars"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }
        String token = authHeader.substring(7);
        return jwtPersistencePort.validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        return createSecurityContext(token)
                                .flatMap(authentication ->
                                        chain.filter(exchange)
                                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                );
                    } else {
                        return unauthorized(exchange);
                    }
                })
                .onErrorResume(error -> {
                    log.error("Error validating token: {}", error.getMessage());
                    return unauthorized(exchange);
                });
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<UsernamePasswordAuthenticationToken> createSecurityContext(String token) {
        return Mono.zip(
                jwtPersistencePort.getUserIdFromToken(token),
                jwtPersistencePort.getEmailFromToken(token),
                jwtPersistencePort.getRoleFromToken(token)
        ).map(tuple -> {
            String userId = tuple.getT1();
            String email = tuple.getT2();
            String role = tuple.getT3();

            String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);

            return new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singletonList(grantedAuthority)
            );
        });
    }
}