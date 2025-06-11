package com.arka.micro_user.data;

import com.arka.micro_user.adapters.driven.r2dbc.entity.UserEntity;
import com.arka.micro_user.adapters.driving.reactive.dto.request.UserRequest;
import com.arka.micro_user.domain.model.UserModel;

import java.time.LocalDateTime;

public class UserData {
    public static UserModel getAdminUser() {
        UserModel user = new UserModel();
        user.setId(1L);
        user.setDni("100000001");
        user.setFirstName("Alice");
        user.setLastName("Admin");
        user.setEmail("admin@arka.com");
        user.setPassword("encoded-password");
        user.setPhone("+3001234567");
        user.setStatus("ACTIVE");
        user.setRoleId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
    public static UserEntity getAdminUserEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setDni("100000001");
        user.setFirstName("Alice");
        user.setLastName("Admin");
        user.setEmail("admin@arka.com");
        user.setPassword("encoded-password");
        user.setPhone("+3001234567");
        user.setStatus("ACTIVE");
        user.setRoleId(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public static UserRequest getAdminUserRequest() {
        UserRequest user = new UserRequest();
        user.setDni("100000001");
        user.setFirstName("Alice");
        user.setLastName("Admin");
        user.setEmail("admin@arka.com");
        user.setPassword("encoded-password");
        user.setPhone("+3001234567");
        user.setRole("ADMIN");
        return user;
    }

    public static UserModel getClientUser() {
        UserModel user = new UserModel();
        user.setId(2L);
        user.setDni("200000002");
        user.setFirstName("Bob");
        user.setLastName("Client");
        user.setEmail("client@arka.com");
        user.setPassword("encoded-password-client");
        user.setPhone("+3007654321");
        user.setStatus("ACTIVE");
        user.setRoleId(3L);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

}
