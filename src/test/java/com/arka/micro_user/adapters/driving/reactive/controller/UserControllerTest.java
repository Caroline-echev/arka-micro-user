package com.arka.micro_user.adapters.driving.reactive.controller;

import com.arka.micro_user.adapters.driving.reactive.dto.request.ChangePasswordRequest;
import com.arka.micro_user.adapters.driving.reactive.dto.request.UserRequest;
import com.arka.micro_user.adapters.driving.reactive.mapper.IUserDtoMapper;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.api.IUserServicePort;


import com.arka.micro_user.domain.model.UserModel;
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
class UserControllerTest {

    @Mock
    private IUserServicePort userService;

    @Mock
    private IUserDtoMapper userMapper;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserModel user;

    @BeforeEach
    void setUp() {
        userRequest = UserData.getAdminUserRequest();

        user = UserData.getAdminUser();
    }

    @Test
    void testCreateUserAdminLogistic() {
        when(userMapper.toModel(userRequest)).thenReturn(user);
        when(userService.createUserAdminLogistic(user, "ADMIN")).thenReturn(Mono.empty());

        Mono<Void> result = userController.createUserAdminLogistic(userRequest);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userMapper).toModel(userRequest);
        verify(userService).createUserAdminLogistic(user, "ADMIN");
    }

    @Test
    void testCreateUserClient() {
        when(userMapper.toModel(userRequest)).thenReturn(user);
        when(userService.createUserClient(user)).thenReturn(Mono.empty());

        Mono<Void> result = userController.createUserClient(userRequest);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userMapper).toModel(userRequest);
        verify(userService).createUserClient(user);
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setEmail("test@example.com");
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");

        when(userService.changeUserPassword("test@example.com", "oldPass", "newPass"))
                .thenReturn(Mono.empty());

        Mono<Void> result = userController.changePassword(request);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userService).changeUserPassword("test@example.com", "oldPass", "newPass");
    }
}
