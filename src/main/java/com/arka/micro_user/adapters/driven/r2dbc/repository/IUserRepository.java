package com.arka.micro_user.adapters.driven.r2dbc.repository;

import com.arka.micro_user.adapters.driven.r2dbc.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface IUserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByDni(String dni);
}
