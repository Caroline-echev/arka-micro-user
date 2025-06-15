package com.arka.micro_user.domain.util.validation;

import com.arka.micro_user.domain.enums.UserRole;
import com.arka.micro_user.domain.exception.BadRequestException;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.RoleModel;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.arka.micro_user.domain.util.constants.AddressConstants.INVALID_ROLE_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.constants.UserConstants.*;

@Slf4j
public class UserValidationUtil {

    public static Mono<RoleModel> validateRoleAdminLogistic(String roleName, IRolePersistencePort rolePersistencePort) {
        log.debug("Validating if role '{}' is ADMIN or LOGISTIC", roleName);

        if (!roleName.equals(UserRole.ADMIN.name()) && !roleName.equals(UserRole.LOGISTIC.name())) {
            log.warn("Invalid role: {}", roleName);
            return Mono.error(new BadRequestException(INVALID_ROLE_EXCEPTION_MESSAGE));
        }

        log.debug("Role '{}' is valid. Fetching from persistence layer...", roleName);
        return rolePersistencePort.getRoleByName(roleName)
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Role '{}' not found in persistence", roleName);
                    return Mono.error(new NotFoundException(ROLE_DOES_NOT_EXIST_EXCEPTION_MESSAGE));
                }));
    }

    public static Mono<Void> validateUserDoesNotExistByEmail(String email, IUserPersistencePort userPersistencePort) {
        log.debug("Checking if user with email '{}' already exists", email);
        return userPersistencePort.findByEmail(email)
                .flatMap(user -> {
                    if (user != null) {
                        log.warn("User already exists with email: {}", email);
                        return Mono.error(new DuplicateResourceException(USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION_MESSAGE + email));
                    }
                    return Mono.empty();
                });
    }

    public static Mono<Void> validateUserDoesNotExistByDni(String dni, IUserPersistencePort userPersistencePort) {
        log.debug("Checking if user with DNI '{}' already exists", dni);
        return userPersistencePort.findByDni(dni)
                .flatMap(user -> {
                    if (user != null) {
                        log.warn("User already exists with DNI: {}", dni);
                        return Mono.error(new DuplicateResourceException(USER_ALREADY_EXISTS_BY_DNI_EXCEPTION_MESSAGE + dni));
                    }
                    return Mono.empty();
                });
    }

    public static Mono<Void> validateRoleClient(UserModel user, IRolePersistencePort rolePersistencePort) {
        log.debug("Validating if user with ID={} has role CLIENT (expected role ID={})", user.getId(), user.getRoleId());

        return rolePersistencePort.getRoleByName(UserRole.CLIENT.name())
                .filter(role -> {
                    boolean matches = role.getId().equals(user.getRoleId());
                    if (!matches) {
                        log.warn("User ID={} does not have CLIENT role. Expected role ID={}, but got {}", user.getId(), role.getId(), user.getRoleId());
                    }
                    return matches;
                })
                .switchIfEmpty(Mono.error(new BadRequestException(INVALID_ROLE_EXCEPTION_MESSAGE)))
                .then();
    }
}
