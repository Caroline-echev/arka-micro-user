package com.arka.micro_user.adapters.driven.security.adapter;

import com.arka.micro_user.domain.spi.IAuthenticationPersistencePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;

@Slf4j
@Component
public class AuthenticationAdapter implements IAuthenticationPersistencePort {

    @Override
    public Mono<String> getAuthenticatedEmail() {
        log.info("Attempting to retrieve authenticated email from security context");
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    log.debug("Authentication object: {}", auth);
                    return auth;
                })
                .map(Authentication::getPrincipal)
                .cast(String.class)
                .doOnSuccess(email -> log.info("Authenticated email retrieved: {}", email))
                .doOnError(error -> log.error("Error retrieving authenticated email: {}", error.getMessage()));
    }
}
