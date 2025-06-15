package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IUserEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IUserRepository;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAdapter implements IUserPersistencePort {

    private static final String USER_ENTITY_FOUND_MSG = "User entity found: {}";

    private final IUserRepository userRepository;
    private final IUserEntityMapper userMapper;

    @Override
    public Mono<UserModel> saveUser(UserModel user) {
        log.info("Saving user with email: {}", user.getEmail());
        return userRepository.save(userMapper.toEntity(user))
                .doOnSuccess(entity -> log.info("User saved successfully: {}", entity.getId()))
                .map(userMapper::toModel)
                .doOnError(error -> log.error("Error saving user with email {}: {}", user.getEmail(), error.getMessage()));
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        log.info("Looking up user by email: {}", email);
        return userRepository.findByEmail(email)
                .doOnNext(entity -> log.debug(USER_ENTITY_FOUND_MSG, entity))
                .map(userMapper::toModel)
                .doOnSuccess(model -> log.info("User found and mapped successfully for email: {}", email))
                .doOnError(error -> log.error("Error finding user by email {}: {}", email, error.getMessage()));
    }

    @Override
    public Mono<UserModel> findByDni(String dni) {
        log.info("Looking up user by DNI: {}", dni);
        return userRepository.findByDni(dni)
                .doOnNext(entity -> log.debug(USER_ENTITY_FOUND_MSG, entity))
                .map(userMapper::toModel)
                .doOnSuccess(model -> log.info("User found and mapped successfully for DNI: {}", dni))
                .doOnError(error -> log.error("Error finding user by DNI {}: {}", dni, error.getMessage()));
    }

    @Override
    public Mono<UserModel> findById(Long id) {
        log.info("Looking up user by ID: {}", id);
        return userRepository.findById(id)
                .doOnNext(entity -> log.debug(USER_ENTITY_FOUND_MSG, entity))
                .map(userMapper::toModel)
                .doOnSuccess(model -> log.info("User found and mapped successfully for ID: {}", id))
                .doOnError(error -> log.error("Error finding user by ID {}: {}", id, error.getMessage()));
    }
}
