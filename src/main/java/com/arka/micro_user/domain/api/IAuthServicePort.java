package com.arka.micro_user.domain.api;

import com.arka.micro_user.domain.model.AuthenticationModel;
import reactor.core.publisher.Mono;

public interface IAuthServicePort {

    Mono<String> authenticate(AuthenticationModel authModel);

    Mono<String> refreshToken(String token);
    Long    getExpirationTime();
}
