package com.arka.micro_user.adapters.driving.reactive.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String type;
    private Long expiresIn;
}