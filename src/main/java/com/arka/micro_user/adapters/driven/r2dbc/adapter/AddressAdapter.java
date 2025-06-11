package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IAddressEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IAddressRepository;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AddressAdapter implements IAddressPersistencePort {
    private final IAddressRepository addressRepository;
    private final IAddressEntityMapper addressEntityMapper;
    @Override
    public Mono<AddressModel> findByUserId(Long userId) {
        return addressRepository.findByUserId(userId).map(addressEntityMapper::toModel);
    }

    @Override
    public Mono<Void> save(AddressModel addressModel) {
        return addressRepository.save(addressEntityMapper.toEntity(addressModel)).then();
    }
}
