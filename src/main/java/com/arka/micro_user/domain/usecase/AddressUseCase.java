package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.domain.api.IAddressServicePort;
import com.arka.micro_user.domain.exception.BadRequestException;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import com.arka.micro_user.domain.spi.IAuthenticationPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import com.arka.micro_user.domain.util.validation.UserValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.arka.micro_user.domain.util.constants.AddressConstants.CUSTOMER_ALREADY_HAS_ADDRESS_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.constants.AddressConstants.USER_DOES_NOT_HAVE_ADDRESS_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.constants.UserConstants.USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE;
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressUseCase implements IAddressServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IAddressPersistencePort addressPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IAuthenticationPersistencePort authenticationPersistencePort;

    @Override
    public Mono<Void> createAddress(AddressModel addressModel, String dni) {
        log.info("Starting address creation for DNI: {}", dni);
        return validateUserIdentity(dni)
                .flatMap(user -> {
                    log.debug("User validated: ID={}, checking if address exists...", user.getId());
                    return UserValidationUtil.validateRoleClient(user, rolePersistencePort)
                            .then(addressPersistencePort.findByUserId(user.getId())
                                    .flatMap(existing -> {
                                        log.warn("User ID={} already has an address", user.getId());
                                        return Mono.error(new DuplicateResourceException(CUSTOMER_ALREADY_HAS_ADDRESS_EXCEPTION_MESSAGE));
                                    })
                                    .switchIfEmpty(Mono.defer(() -> {
                                        log.info("Saving address for user ID={}", user.getId());
                                        addressModel.setUserId(user.getId());
                                        return addressPersistencePort.save(addressModel)
                                                .doOnSuccess(saved -> log.info("Address created for user ID={}", user.getId()));
                                    }))
                            );
                }).then();
    }

    @Override
    public Mono<Void> updateAddress(AddressModel addressModel, String dni) {
        log.info("Starting address update for DNI: {}", dni);
        return validateUserIdentity(dni)
                .flatMap(user -> {
                    log.debug("User validated: ID={}, checking address to update...", user.getId());
                    return UserValidationUtil.validateRoleClient(user, rolePersistencePort)
                            .then(addressPersistencePort.findByUserId(user.getId())
                                    .switchIfEmpty(Mono.defer(() -> {
                                        log.warn("No address found for user ID={}", user.getId());
                                        return Mono.error(new NotFoundException(USER_DOES_NOT_HAVE_ADDRESS_EXCEPTION_MESSAGE));
                                    }))
                                    .flatMap(existingAddress -> {
                                        log.info("Updating address ID={} for user ID={}", existingAddress.getId(), user.getId());
                                        addressModel.setId(existingAddress.getId());
                                        addressModel.setUserId(user.getId());
                                        return addressPersistencePort.save(addressModel)
                                                .doOnSuccess(saved -> log.info("Address updated for user ID={}", user.getId()));
                                    })
                            );
                }).then();
    }


    private Mono<UserModel> validateUserIdentity(String dni) {
        log.debug("Validating user identity for DNI: {}", dni);
        return authenticationPersistencePort.getAuthenticatedEmail()
                .flatMap(authEmail -> {
                    log.debug("Authenticated email: {}", authEmail);
                    return userPersistencePort.findByEmail(authEmail)
                            .switchIfEmpty(Mono.error(new NotFoundException("Authenticated user not found with email: " + authEmail)))
                            .flatMap(authUser ->
                                    userPersistencePort.findByDni(dni)
                                            .switchIfEmpty(Mono.error(new NotFoundException(USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE)))
                                            .flatMap(userByDni -> {
                                                if (!authUser.getId().equals(userByDni.getId())) {
                                                    log.warn("Access denied: User ID={} trying to modify data of user ID={}", authUser.getId(), userByDni.getId());
                                                    return Mono.error(new BadRequestException("You are not authorized to modify this address."));
                                                }
                                                return Mono.just(userByDni);
                                            })
                            );
                });
    }
}
