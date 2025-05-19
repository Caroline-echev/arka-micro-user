package com.arka.micro_user.domain.spi;

import com.arka.micro_user.domain.model.AddressModel;
import reactor.core.publisher.Mono;

public interface IAddressPersistencePort {

    Mono<AddressModel> findByUserId(Long userId);

    Mono<Void> save(AddressModel addressModel);
}
