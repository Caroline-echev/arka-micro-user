package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IAddressEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IAddressRepository;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressAdapter implements IAddressPersistencePort {

    private final IAddressRepository addressRepository;
    private final IAddressEntityMapper addressEntityMapper;

    @Override
    public Mono<AddressModel> findByUserId(Long userId) {
        log.info("Looking for address with user ID: {}", userId);
        return addressRepository.findByUserId(userId)
                .doOnNext(entity -> log.debug("Address entity found: {}", entity))
                .map(addressEntityMapper::toModel)
                .doOnSuccess(model -> log.info("Successfully mapped address entity to model for user ID: {}", userId))
                .doOnError(error -> log.error("Error retrieving address for user ID {}: {}", userId, error.getMessage()));
    }

    @Override
    public Mono<Void> save(AddressModel addressModel) {
        log.info("Saving address for user ID: {}", addressModel.getUserId());
        return addressRepository.save(addressEntityMapper.toEntity(addressModel))
                .doOnSuccess(saved -> log.info("Address successfully saved for user ID: {}", addressModel.getUserId()))
                .doOnError(error -> log.error("Error saving address for user ID {}: {}", addressModel.getUserId(), error.getMessage()))
                .then();
    }
}
