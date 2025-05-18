package com.arka.micro_user.adapters.driving.reactive.controller;

import com.arka.micro_user.adapters.driving.reactive.dto.request.UserRequest;
import com.arka.micro_user.adapters.driving.reactive.mapper.IUserDtoMapper;
import com.arka.micro_user.domain.api.IUserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints para gestión de usuarios")
public class UserController {

    private final IUserServicePort userService;
    private final IUserDtoMapper userMapper;

    @PostMapping("/admin-logistic")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new administrator or logistics user")
    public Mono<Void> createUserAdminLogistic(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUserAdminLogistic(userMapper.toModel(userRequest), userRequest.getRole()).then();
    }

    @PostMapping("/client")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new client user")
    public Mono<Void> createUserClient(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUserClient(userMapper.toModel(userRequest)).then();
    }

}