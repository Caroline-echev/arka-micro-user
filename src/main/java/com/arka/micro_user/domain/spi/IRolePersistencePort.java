package com.arka.micro_user.domain.spi;

import com.arka.micro_user.domain.model.RoleModel;
import reactor.core.publisher.Mono;

public interface IRolePersistencePort {

    Mono<RoleModel> getRoleByName(String name);
    Mono<RoleModel> getRoleById (Long id);
}
