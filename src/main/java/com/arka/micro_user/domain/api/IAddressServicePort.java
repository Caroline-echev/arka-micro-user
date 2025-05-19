package com.arka.micro_user.domain.api;

import com.arka.micro_user.domain.model.AddressModel;
import reactor.core.publisher.Mono;

public interface IAddressServicePort {

    Mono<Void> createAddress(AddressModel request, String dni);
}
