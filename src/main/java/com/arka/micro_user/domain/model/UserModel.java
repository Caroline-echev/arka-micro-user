package com.arka.micro_user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import static com.arka.micro_user.domain.util.UserConstants.*;


@Data
public class UserModel {
    private Long id;
    private String dni;
    private String firstName ;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String status;
    private Long roleId;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public void setDni(String dni) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException(DNI_REQUIRED);
        if (!dni.matches(REGEX_NUMERIC)) throw new IllegalArgumentException(DNI_NUMERIC);
        this.dni = dni;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException(FIRST_NAME_REQUIRED);
        if (firstName.length() > NAME_MAX_LENGTH) throw new IllegalArgumentException(FIRST_NAME_MAX);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException(LAST_NAME_REQUIRED);
        if (lastName.length() > NAME_MAX_LENGTH) throw new IllegalArgumentException(LAST_NAME_MAX);
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException(EMAIL_REQUIRED);
        if (!email.matches(REGEX_EMAIL)) throw new IllegalArgumentException(EMAIL_INVALID);
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException(PASSWORD_REQUIRED);
        this.password = password;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException(PHONE_REQUIRED);
        if (!phone.matches(REGEX_PHONE)) throw new IllegalArgumentException(PHONE_FORMAT);
        this.phone = phone;
    }
}
