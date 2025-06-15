package com.arka.micro_user.domain.spi;

import reactor.core.publisher.Mono;

public interface IAuthenticationPersistencePort {
    Mono<String> getAuthenticatedEmail();
}
