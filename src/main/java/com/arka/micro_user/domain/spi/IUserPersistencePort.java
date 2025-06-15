package com.arka.micro_user.domain.spi;

import com.arka.micro_user.domain.model.UserModel;
import reactor.core.publisher.Mono;

public interface IUserPersistencePort {
    Mono<UserModel> saveUser(UserModel user);
    Mono<UserModel> findByEmail(String email);
    Mono<UserModel> findByDni(String dni);
    Mono<UserModel> findById(Long id);
}
