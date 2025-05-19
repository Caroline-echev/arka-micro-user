package com.arka.micro_user.adapters.driving.reactive.controller;

import com.arka.micro_user.adapters.driving.reactive.dto.request.AddressRequest;
import com.arka.micro_user.adapters.driving.reactive.mapper.IAddressDtoMapper;
import com.arka.micro_user.data.AddressData;
import com.arka.micro_user.domain.api.IAddressServicePort;
import com.arka.micro_user.domain.model.AddressModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private IAddressServicePort addressServicePort;

    @Mock
    private IAddressDtoMapper addressDtoMapper;

    @InjectMocks
    private AddressController addressController;

    private AddressRequest addressRequest;
    private AddressModel address;

    @BeforeEach
    void setUp() {
        addressRequest = AddressData.getClientAddressRequest();

        address = AddressData.getClientAddress();
    }

    @Test
    void testCreateAddress() {
        String dni = "123456";
        when(addressDtoMapper.toModel(addressRequest)).thenReturn(address);
        when(addressServicePort.createAddress(address, dni)).thenReturn(Mono.empty());

        Mono<Void> result = addressController.createAddress(addressRequest, dni);

        StepVerifier.create(result)
                .verifyComplete();

        verify(addressDtoMapper).toModel(addressRequest);
        verify(addressServicePort).createAddress(address, dni);
    }

    @Test
    void testUpdateAddress() {
        String dni = "123456";
        when(addressDtoMapper.toModel(addressRequest)).thenReturn(address);
        when(addressServicePort.updateAddress(address, dni)).thenReturn(Mono.empty());

        Mono<Void> result = addressController.updateAddress(addressRequest, dni);

        StepVerifier.create(result)
                .verifyComplete();

        verify(addressDtoMapper).toModel(addressRequest);
        verify(addressServicePort).updateAddress(address, dni);
    }
}
