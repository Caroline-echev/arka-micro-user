package com.arka.micro_user.domain.api;

import com.arka.micro_user.domain.model.UserModel;
import reactor.core.publisher.Mono;

public interface IUserServicePort {
    Mono<UserModel> createUserAdminLogistic(UserModel userModel, String role);

    Mono<UserModel> createUserClient(UserModel userModel);
    Mono<Void> changeUserPassword(String email, String oldPassword, String newPassword);
    Mono<Boolean> existsByIdAndValidRole (Long id, String role);
    Mono<UserModel> findByEmail(String email);
}
