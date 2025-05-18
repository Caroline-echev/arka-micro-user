package com.arka.micro_user.adapters.driven.security.adapter;

import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PasswordEncoderAdapter implements IPasswordEncoderPersistencePort {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Mono<String> encodePassword(String password) {
        return Mono.fromCallable(() -> passwordEncoder.encode(password));
    }

}
