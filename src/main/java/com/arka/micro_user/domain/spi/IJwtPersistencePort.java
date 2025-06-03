package com.arka.micro_user.domain.spi;

import reactor.core.publisher.Mono;

public  interface IJwtPersistencePort {

    Mono<String> generateToken(String userId, String email, String role);
    Mono<Boolean> validateToken(String token);
    Long getExpirationTime();
    Mono<String> refreshToken(String token);


    Mono<String> getUserIdFromToken(String token);

    Mono<String> getEmailFromToken(String token);

    Mono<String> getRoleFromToken(String token);
}
