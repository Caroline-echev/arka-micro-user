package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IRoleEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IRoleRepository;
import com.arka.micro_user.domain.model.RoleModel;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleMapper;

    @Override
    public Mono<RoleModel> getRoleByName(String name) {
        log.info("Fetching role by name: {}", name);
        return roleRepository.findByName(name)
                .doOnNext(entity -> log.debug("Role entity found: {}", entity))
                .map(roleMapper::toModel)
                .doOnSuccess(role -> log.info("Successfully mapped role '{}' to domain model", name))
                .doOnError(error -> log.error("Error retrieving role by name '{}': {}", name, error.getMessage()));
    }

    @Override
    public Mono<RoleModel> getRoleById(Long id) {
        log.info("Fetching role by ID: {}", id);
        return roleRepository.findById(id)
                .doOnNext(entity -> log.debug("Role entity found: {}", entity))
                .map(roleMapper::toModel)
                .doOnSuccess(role -> log.info("Successfully mapped role ID {} to domain model", id))
                .doOnError(error -> log.error("Error retrieving role by ID {}: {}", id, error.getMessage()));
    }
}
