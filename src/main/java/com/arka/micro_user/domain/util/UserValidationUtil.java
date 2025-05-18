    package com.arka.micro_user.domain.util;

    import com.arka.micro_user.domain.enums.UserRole;
    import com.arka.micro_user.domain.exception.BadRequestException;
    import com.arka.micro_user.domain.exception.DuplicateResourceException;
    import com.arka.micro_user.domain.exception.NotFoundException;
    import com.arka.micro_user.domain.model.RoleModel;
    import com.arka.micro_user.domain.spi.IRolePersistencePort;
    import com.arka.micro_user.domain.spi.IUserPersistencePort;
    import reactor.core.publisher.Mono;

    import static com.arka.micro_user.domain.util.UserConstants.*;

    public class  UserValidationUtil{

        public static Mono<RoleModel> validateRoleAdminLogistic(String roleName, IRolePersistencePort rolePersistencePort) {
            return (!roleName.equals(UserRole.ADMIN.name()) && !roleName.equals(UserRole.LOGISTIC.name()))
                    ? Mono.error(new BadRequestException(INVALID_ROLE_EXCEPTION_MESSAGE))
                    : rolePersistencePort.getRoleByName(roleName)
                    .switchIfEmpty(Mono.error(new NotFoundException(ROLE_DOES_NOT_EXIST_EXCEPTION_MESSAGE)));
        }


        public static Mono<Void> validateUserDoesNotExistByEmail(String email, IUserPersistencePort userPersistencePort) {
            return userPersistencePort.findByEmail(email)
                    .flatMap(user -> {
                        if (user != null) {
                            return Mono.error(new DuplicateResourceException(USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION_MESSAGE + email));
                        }
                        return Mono.empty();
                    });
        }

        public static Mono<Void> validateUserDoesNotExistByDni(String dni, IUserPersistencePort userPersistencePort) {
            return userPersistencePort.findByDni(dni)
                    .flatMap(user -> {
                        if (user != null) {
                            return Mono.error(new DuplicateResourceException(USER_ALREADY_EXISTS_BY_DNI_EXCEPTION_MESSAGE + dni));
                        }
                        return Mono.empty();
                    });
        }
    }
