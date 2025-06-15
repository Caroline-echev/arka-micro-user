package com.arka.micro_user.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.arka.micro_user.adapters.util.AuthConstantsAdapter.REFRESH_TOKEN_REQUIRED;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = REFRESH_TOKEN_REQUIRED)
    private String token;
}
