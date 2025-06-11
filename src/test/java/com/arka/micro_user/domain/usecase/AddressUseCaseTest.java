package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.data.AddressData;
import com.arka.micro_user.data.RoleData;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IAddressPersistencePort addressPersistencePort;

    @Mock
    private IRolePersistencePort rolePersistencePort;

    @InjectMocks
    private AddressUseCase addressUseCase;

    private UserModel clientUser;
    private AddressModel address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientUser = UserData.getClientUser();
        address = AddressData.getClientAddress();
    }

    @Test
    void createAddress_success() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.just(clientUser));

        when(rolePersistencePort.getRoleByName("CLIENT"))
                .thenReturn(Mono.just(RoleData.getClientRole()));

        when(addressPersistencePort.findByUserId(clientUser.getId()))
                .thenReturn(Mono.empty());


        when(addressPersistencePort.save(any(AddressModel.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(addressUseCase.createAddress(address, clientUser.getDni()))
                .verifyComplete();

        verify(addressPersistencePort, times(1)).save(any(AddressModel.class));
    }

    @Test
    void createAddress_userNotFound() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.empty());

        StepVerifier.create(addressUseCase.createAddress(address, clientUser.getDni()))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void createAddress_alreadyHasAddress() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.just(clientUser));

        when(rolePersistencePort.getRoleByName("CLIENT"))
                .thenReturn(Mono.just(RoleData.getClientRole()));

        when(addressPersistencePort.findByUserId(clientUser.getId()))
                .thenReturn(Mono.just(address));

        StepVerifier.create(addressUseCase.createAddress(address, clientUser.getDni()))
                .expectError(DuplicateResourceException.class)
                .verify();
    }

    @Test
    void updateAddress_success() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.just(clientUser));

        when(rolePersistencePort.getRoleByName("CLIENT"))
                .thenReturn(Mono.just(RoleData.getClientRole()));


        when(addressPersistencePort.findByUserId(clientUser.getId()))
                .thenReturn(Mono.just(address));

        when(addressPersistencePort.save(any(AddressModel.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(addressUseCase.updateAddress(address, clientUser.getDni()))
                .verifyComplete();

        verify(addressPersistencePort, times(1)).save(any(AddressModel.class));
    }

    @Test
    void updateAddress_userNotFound() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.empty());

        StepVerifier.create(addressUseCase.updateAddress(address, clientUser.getDni()))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void updateAddress_noAddressFound() {
        when(userPersistencePort.findByDni(clientUser.getDni()))
                .thenReturn(Mono.just(clientUser));

        when(rolePersistencePort.getRoleByName("CLIENT"))
                .thenReturn(Mono.just(RoleData.getClientRole()));


        when(addressPersistencePort.findByUserId(clientUser.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(addressUseCase.updateAddress(address, clientUser.getDni()))
                .expectError(NotFoundException.class)
                .verify();
    }
}
