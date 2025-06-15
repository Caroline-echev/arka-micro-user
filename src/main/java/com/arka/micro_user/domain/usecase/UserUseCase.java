package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.domain.api.IUserServicePort;
import com.arka.micro_user.domain.enums.UserRole;
import com.arka.micro_user.domain.enums.UserStatus;
import com.arka.micro_user.domain.exception.BadRequestException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import com.arka.micro_user.domain.util.validation.UserValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.arka.micro_user.domain.util.constants.UserConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    @Override
    public Mono<UserModel> createUserAdminLogistic(UserModel userModel, String role) {
        log.info("Creating admin/logistic user with email: {} and role: {}", userModel.getEmail(), role);

        return UserValidationUtil.validateRoleAdminLogistic(role, rolePersistencePort)
                .flatMap(roleModel -> {
                    userModel.setRoleId(roleModel.getId());
                    log.debug("Role validated and set with ID: {}", roleModel.getId());

                    return UserValidationUtil.validateUserDoesNotExistByEmail(userModel.getEmail(), userPersistencePort)
                            .doOnSuccess(unused -> log.debug("Email {} does not exist in system", userModel.getEmail()))
                            .then(UserValidationUtil.validateUserDoesNotExistByDni(userModel.getDni(), userPersistencePort))
                            .doOnSuccess(unused -> log.debug("DNI {} does not exist in system", userModel.getDni()))
                            .then(Mono.defer(() -> {
                                userModel.setStatus(UserStatus.ACTIVE.name());
                                userModel.setCreatedAt(LocalDateTime.now());
                                log.debug("User model initialized with ACTIVE status and createdAt timestamp");
                                return Mono.just(userModel);
                            }))
                            .flatMap(this::encodePassword)
                            .flatMap(user -> {
                                log.info("Saving new admin/logistic user with email: {}", user.getEmail());
                                return userPersistencePort.saveUser(user);
                            });
                });
    }

    @Override
    public Mono<UserModel> createUserClient(UserModel userModel) {
        log.info("Creating client user with email: {}", userModel.getEmail());

        return rolePersistencePort.getRoleByName(UserRole.CLIENT.name())
                .switchIfEmpty(Mono.error(new NotFoundException(ROLE_DOES_NOT_EXIST_EXCEPTION_MESSAGE)))
                .flatMap(roleModel -> {
                    userModel.setRoleId(roleModel.getId());
                    log.debug("Client role set with ID: {}", roleModel.getId());

                    return UserValidationUtil.validateUserDoesNotExistByEmail(userModel.getEmail(), userPersistencePort)
                            .doOnSuccess(unused -> log.debug("Email {} does not exist", userModel.getEmail()))
                            .then(UserValidationUtil.validateUserDoesNotExistByDni(userModel.getDni(), userPersistencePort))
                            .doOnSuccess(unused -> log.debug("DNI {} does not exist", userModel.getDni()))
                            .then(Mono.defer(() -> {
                                userModel.setStatus(UserStatus.ACTIVE.name());
                                userModel.setCreatedAt(LocalDateTime.now());
                                log.debug("User model initialized with ACTIVE status and createdAt timestamp");
                                return Mono.just(userModel);
                            }))
                            .flatMap(this::encodePassword)
                            .flatMap(user -> {
                                log.info("Saving new client user with email: {}", user.getEmail());
                                return userPersistencePort.saveUser(user);
                            });
                });
    }

    @Override
    public Mono<Void> changeUserPassword(String email, String oldPassword, String newPassword) {
        log.info("Request to change password for user: {}", email);

        return userPersistencePort.findByEmail(email)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("User not found with email: {}", email);
                    return Mono.error(new NotFoundException(USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE + email));
                }))
                .flatMap(user -> {
                    boolean matches = passwordEncoderPersistencePort.matches(oldPassword, user.getPassword());

                    if (!matches) {
                        log.warn("Invalid current password for user: {}", email);
                        return Mono.error(new BadRequestException(INVALID_PASSWORD_EXCEPTION_MESSAGE));
                    }

                    log.debug("Current password validated for user: {}", email);

                    return passwordEncoderPersistencePort.encodePassword(newPassword)
                            .flatMap(encoded -> {
                                user.setPassword(encoded);
                                log.info("Saving updated password for user: {}", email);
                                return userPersistencePort.saveUser(user).then();
                            });
                });
    }

    @Override
    public Mono<Boolean> existsByIdAndValidRole(Long id, String role) {
        log.debug("Checking if user with ID {} has role {}", id, role);

        return userPersistencePort.findById(id)
                .flatMap(user ->
                        rolePersistencePort.getRoleById(user.getRoleId())
                                .map(roleModel -> {
                                    boolean matches = role.equals(roleModel.getName());
                                    log.debug("Role match for user {}: {}", id, matches);
                                    return matches;
                                })
                );
    }

    private Mono<UserModel> encodePassword(UserModel userModel) {
        log.debug("Encoding password for user: {}", userModel.getEmail());

        return passwordEncoderPersistencePort.encodePassword(userModel.getPassword())
                .map(encodedPassword -> {
                    userModel.setPassword(encodedPassword);
                    log.debug("Password encoded for user: {}", userModel.getEmail());
                    return userModel;
                });
    }
}
