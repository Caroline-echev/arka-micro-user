package com.arka.micro_user.domain.spi;

import reactor.core.publisher.Mono;

public interface IPasswordEncoderPersistencePort {
    Mono<String> encodePassword(String password);
    boolean matches(String rawPassword, String encodedPassword);
}
