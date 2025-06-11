package com.arka.micro_user.adapters.driving.reactive.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.arka.micro_user.adapters.util.UserConstantsAdapter.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {


    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    private String password;
}
