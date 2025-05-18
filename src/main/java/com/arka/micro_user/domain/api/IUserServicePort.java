package com.arka.micro_user.domain.api;

import com.arka.micro_user.domain.model.UserModel;
import reactor.core.publisher.Mono;

public interface IUserServicePort {
    Mono<UserModel> createUserAdminLogistic(UserModel userModel, String role);



}
