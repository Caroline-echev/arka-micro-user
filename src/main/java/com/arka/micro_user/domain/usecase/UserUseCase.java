package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.domain.api.IUserServicePort;
import com.arka.micro_user.domain.enums.UserRole;
import com.arka.micro_user.domain.enums.UserStatus;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import com.arka.micro_user.domain.util.UserValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.arka.micro_user.adapters.util.UserConstantsAdapter.ROLE_CLIENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    @Override
    public Mono<UserModel> createUserAdminLogistic(UserModel userModel, String role) {
        return UserValidationUtil.validateRoleAdminLogistic(role, rolePersistencePort)
                .flatMap(roleModel -> {
                    userModel.setRoleId(roleModel.getId());
                    return UserValidationUtil.validateUserDoesNotExistByEmail(userModel.getEmail(), userPersistencePort)
                            .then(UserValidationUtil.validateUserDoesNotExistByDni(userModel.getDni(), userPersistencePort))
                            .then(Mono.defer(() -> {
                                userModel.setStatus(UserStatus.ACTIVE.name());
                                userModel.setCreatedAt(LocalDateTime.now());
                                return Mono.just(userModel);
                            }))
                            .flatMap(this::encodePassword)
                            .flatMap(userPersistencePort::saveUser);
                });
    }

    @Override
    public Mono<UserModel> createUserClient(UserModel userModel) {
        return rolePersistencePort.getRoleByName(UserRole.CLIENT.name()).switchIfEmpty(Mono.error(new NotFoundException(ROLE_CLIENT_NOT_FOUND)))
                .flatMap(roleModel -> {
                    userModel.setRoleId(roleModel.getId());
                    return UserValidationUtil.validateUserDoesNotExistByEmail(userModel.getEmail(), userPersistencePort)
                            .then(UserValidationUtil.validateUserDoesNotExistByDni(userModel.getDni(), userPersistencePort))
                            .then(Mono.defer(() -> {
                                userModel.setStatus(UserStatus.ACTIVE.name());
                                userModel.setCreatedAt(LocalDateTime.now());
                                return Mono.just(userModel);
                            }))
                            .flatMap(this::encodePassword)
                            .flatMap(userPersistencePort::saveUser);
                });
    }

    private Mono<UserModel> encodePassword(UserModel userModel) {
        return passwordEncoderPersistencePort.encodePassword(userModel.getPassword())
                .map(encodedPassword -> {
                    userModel.setPassword(encodedPassword);
                    return userModel;
                });
    }







}
