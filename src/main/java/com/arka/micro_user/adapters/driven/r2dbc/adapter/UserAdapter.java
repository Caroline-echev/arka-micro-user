package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IUserEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IUserRepository;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userMapper;

    @Override
    public Mono<UserModel> saveUser(UserModel user) {
        return userRepository.save(userMapper.toEntity(user))
                .map(userMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByDni(String dni) {
        return userRepository.findByDni(dni)
                .map(userMapper::toModel);
    }
}
