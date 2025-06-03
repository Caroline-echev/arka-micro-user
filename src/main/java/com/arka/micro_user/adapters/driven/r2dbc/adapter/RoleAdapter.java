package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IRoleEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IRoleRepository;
import com.arka.micro_user.domain.model.RoleModel;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoleAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleMapper;
    @Override
    public Mono<RoleModel> getRoleByName(String name) {

        return roleRepository.findByName(name)
                .map(roleMapper::toModel);
    }

    @Override
    public Mono<RoleModel> getRoleById(Long id) {

        return roleRepository.findById(id)
                .map(roleMapper::toModel);
    }
}
