package com.arka.micro_user.adapters.driven.security.adapter;

import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class PasswordEncoderAdapter implements IPasswordEncoderPersistencePort {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<String> encodePassword(String password) {
        log.info("Encoding password...");
        return Mono.fromCallable(() -> {
            String encoded = passwordEncoder.encode(password);
            log.debug("Password encoded successfully");
            return encoded;
        }).doOnError(error -> log.error("Error encoding password: {}", error.getMessage()));
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        log.info("Verifying password match...");
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);
        log.info("Password match result: {}", result);
        return result;
    }
}
