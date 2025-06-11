package com.arka.micro_user.adapters.driving.reactive.controller;


import com.arka.micro_user.adapters.driving.reactive.dto.request.LoginRequest;
import com.arka.micro_user.adapters.driving.reactive.dto.request.RefreshTokenRequest;
import com.arka.micro_user.adapters.driving.reactive.dto.response.AuthResponse;
import com.arka.micro_user.domain.api.IAuthServicePort;
import com.arka.micro_user.domain.model.AuthenticationModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.arka.micro_user.adapters.util.AuthConstantsAdapter.BEARER;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    private final IAuthServicePort authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User login with email and password")
    public Mono<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationModel authModel = new AuthenticationModel(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        return authService.authenticate(authModel)
                .map(token -> AuthResponse.builder()
                        .token(token)
                        .type(BEARER)
                        .expiresIn(authService.getExpirationTime())
                        .build());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh JWT token")
    public Mono<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        return authService.refreshToken(refreshRequest.getToken())
                .map(newToken -> AuthResponse.builder()
                        .token(newToken)
                        .type(BEARER)
                        .expiresIn(authService.getExpirationTime())
                        .build());
    }
}