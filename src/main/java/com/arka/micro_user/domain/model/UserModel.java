package com.arka.micro_user.domain.model;

import com.arka.micro_user.domain.exception.BadRequestException;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setDni(String dni) {
        if (dni == null || dni.isBlank()) throw new BadRequestException(DNI_REQUIRED);
        if (!dni.matches(REGEX_NUMERIC)) throw new BadRequestException(DNI_NUMERIC);
        this.dni = dni;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new BadRequestException(FIRST_NAME_REQUIRED);
        if (firstName.length() > NAME_MAX_LENGTH) throw new BadRequestException(FIRST_NAME_MAX);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new BadRequestException(LAST_NAME_REQUIRED);
        if (lastName.length() > NAME_MAX_LENGTH) throw new BadRequestException(LAST_NAME_MAX);
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) throw new BadRequestException(EMAIL_REQUIRED);
        if (!email.matches(REGEX_EMAIL)) throw new BadRequestException(EMAIL_INVALID);
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new BadRequestException(PASSWORD_REQUIRED);
        this.password = password;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.isBlank()) throw new BadRequestException(PHONE_REQUIRED);
        if (!phone.matches(REGEX_PHONE)) throw new BadRequestException(PHONE_FORMAT);
        this.phone = phone;
    }
}
