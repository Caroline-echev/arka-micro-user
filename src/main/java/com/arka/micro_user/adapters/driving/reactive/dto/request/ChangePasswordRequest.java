package com.arka.micro_user.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.arka.micro_user.adapters.util.UserConstantsAdapter.*;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = OLD_PASSWORD_REQUIRED)
    private String oldPassword;

    @NotBlank(message =NEW_PASSWORD_REQUIRED)
    private String newPassword;


}
