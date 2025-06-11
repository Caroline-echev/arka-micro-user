package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.domain.api.IAddressServicePort;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import com.arka.micro_user.domain.util.validation.UserValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.arka.micro_user.domain.util.constants.AddressConstants.CUSTOMER_ALREADY_HAS_ADDRESS_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.constants.AddressConstants.USER_DOES_NOT_HAVE_ADDRESS_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.constants.UserConstants.USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class AddressUseCase implements IAddressServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IAddressPersistencePort addressPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    @Override
    public Mono<Void> createAddress(AddressModel addressModel, String dni) {
        return userPersistencePort.findByDni(dni)
                .switchIfEmpty(Mono.error(new NotFoundException(USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE)))
                .flatMap(user -> UserValidationUtil.validateRoleClient(user, rolePersistencePort)
                        .then(addressPersistencePort.findByUserId(user.getId())
                                .flatMap(existingAddress -> Mono.error(new DuplicateResourceException(CUSTOMER_ALREADY_HAS_ADDRESS_EXCEPTION_MESSAGE)))
                                .switchIfEmpty(Mono.defer(() -> {
                                    addressModel.setUserId(user.getId());
                                    return addressPersistencePort.save(addressModel);
                                }))
                        )
                ).then();
    }

    @Override
    public Mono<Void> updateAddress(AddressModel addressModel, String dni) {
        return userPersistencePort.findByDni(dni)
                .switchIfEmpty(Mono.error(new NotFoundException(USER_DOES_NOT_EXIST_EXCEPTION_MESSAGE)))
                .flatMap(user -> UserValidationUtil.validateRoleClient(user, rolePersistencePort)
                        .then(addressPersistencePort.findByUserId(user.getId())
                                .switchIfEmpty(Mono.error(new NotFoundException(USER_DOES_NOT_HAVE_ADDRESS_EXCEPTION_MESSAGE)))
                                .flatMap(existingAddress -> {
                                    addressModel.setId(existingAddress.getId());
                                    addressModel.setUserId(user.getId());
                                    return addressPersistencePort.save(addressModel);
                                })
                        )
                ).then();
    }

}
