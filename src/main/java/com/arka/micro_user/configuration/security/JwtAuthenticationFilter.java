package com.arka.micro_user.configuration.security;

import com.arka.micro_user.domain.spi.IJwtPersistencePort;
import com.arka.micro_user.domain.exception.BusinessException;
import com.arka.micro_user.domain.exception.error.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

import static com.arka.micro_user.configuration.util.ConstantsConfiguration.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final IJwtPersistencePort jwtPersistencePort;

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/users/client",
            "/api/users/change-password",
            "/api/users/exists/**",
            "/swagger-ui",
            "/v3/api-docs",
            "/api-docs",
            "/webjars"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.debug("Incoming request on path: {}", path);

        if (isExcludedPath(path)) {
            log.debug("Path excluded from JWT filter: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            log.warn("Missing or malformed Authorization header for path: {}", path);
            return Mono.error(new BusinessException(
                    CommonErrorCode.UNAUTHORIZED,
                    "Authorization header is missing or malformed"
            ));
        }

        String token = authHeader.substring(DEFAULT_PAGE_SIZE);
        log.debug("Extracted JWT token: {}", token);

        return jwtPersistencePort.validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        log.debug("Valid JWT token for path: {}", path);
                        return createSecurityContext(token)
                                .doOnNext(auth -> log.info("Security context created for user: {}", auth.getPrincipal()))
                                .flatMap(authentication ->
                                        chain.filter(exchange)
                                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                );
                    } else {
                        log.warn("Invalid JWT token received for path: {}", path);
                        return Mono.error(new BusinessException(
                                CommonErrorCode.UNAUTHORIZED,
                                "Invalid or expired JWT token"
                        ));
                    }
                })
                .onErrorResume(BusinessException.class, Mono::error)
                .onErrorResume(error -> {
                    if (error instanceof BusinessException) {
                        return Mono.error(error);
                    }
                    log.error("Unexpected error validating JWT token: {}", error.getMessage(), error);
                    return Mono.error(new BusinessException(
                            CommonErrorCode.UNAUTHORIZED,
                            "Token validation failed: " + error.getMessage()
                    ));
                });

    }

    private boolean isExcludedPath(String path) {
        boolean excluded = EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
        if (excluded) {
            log.trace("Path {} is excluded from JWT filter", path);
        }
        return excluded;
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

            log.debug("Extracted data from token - userId: {}, email: {}, role: {}", userId, email, role);

            String authority = role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);

            return new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singletonList(grantedAuthority)
            );
        });
    }
}