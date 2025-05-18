package com.arka.micro_user.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.arka.micro_user.adapters.util.UserConstantsAdapter.*;

@Data
public class UserRequest {

    @NotBlank(message = DNI_REQUIRED)
    @Pattern(regexp = REGEX_NUMERIC, message = DNI_NUMERIC)
    private String dni;

    @NotBlank(message = FIRST_NAME_REQUIRED)
    @Size(max = NAME_MAX_LENGTH, message = FIRST_NAME_MAX)
    private String firstName;

    @NotBlank(message = LAST_NAME_REQUIRED)
    @Size(max = NAME_MAX_LENGTH, message = LAST_NAME_MAX)
    private String lastName;

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_INVALID)
    private String email;

    @NotBlank(message = PASSWORD_REQUIRED)
    private String password;

    @NotBlank(message = PHONE_REQUIRED)
    @Pattern(regexp = REGEX_PHONE, message = PHONE_FORMAT)
    private String phone;

    private String role;
}
