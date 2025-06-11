package com.arka.micro_user.adapters.driven.r2dbc.repository;

import com.arka.micro_user.adapters.driven.r2dbc.entity.AddressEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IAddressRepository extends ReactiveCrudRepository<AddressEntity, Long> {

    Mono<AddressEntity> findByUserId(Long userId);
}
