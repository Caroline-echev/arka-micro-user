package com.arka.micro_user.domain.model;

import com.arka.micro_user.domain.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.arka.micro_user.domain.util.constants.UserConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = new UserModel();
    }

    @Test
    void setDni_ShouldSetValue_WhenValid() {
        String validDni = "12345678";
        user.setDni(validDni);
        assertEquals(validDni, user.getDni());
    }

    @Test
    void setDni_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setDni(null));
        assertEquals(DNI_REQUIRED, ex.getMessage());
    }

    @Test
    void setDni_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setDni(" "));
        assertEquals(DNI_REQUIRED, ex.getMessage());
    }

    @Test
    void setDni_ShouldThrowException_WhenNotNumeric() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setDni("ABC123"));
        assertEquals(DNI_NUMERIC, ex.getMessage());
    }


    @Test
    void setFirstName_ShouldSetValue_WhenValid() {
        String validName = "John";
        user.setFirstName(validName);
        assertEquals(validName, user.getFirstName());
    }

    @Test
    void setFirstName_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setFirstName(null));
        assertEquals(FIRST_NAME_REQUIRED, ex.getMessage());
    }

    @Test
    void setFirstName_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setFirstName(" "));
        assertEquals(FIRST_NAME_REQUIRED, ex.getMessage());
    }

    @Test
    void setFirstName_ShouldThrowException_WhenTooLong() {
        String longName = "a".repeat(NAME_MAX_LENGTH + 1);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setFirstName(longName));
        assertEquals(FIRST_NAME_MAX, ex.getMessage());
    }


    @Test
    void setLastName_ShouldSetValue_WhenValid() {
        String validName = "Doe";
        user.setLastName(validName);
        assertEquals(validName, user.getLastName());
    }

    @Test
    void setLastName_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setLastName(null));
        assertEquals(LAST_NAME_REQUIRED, ex.getMessage());
    }

    @Test
    void setLastName_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setLastName(" "));
        assertEquals(LAST_NAME_REQUIRED, ex.getMessage());
    }

    @Test
    void setLastName_ShouldThrowException_WhenTooLong() {
        String longName = "a".repeat(NAME_MAX_LENGTH + 1);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setLastName(longName));
        assertEquals(LAST_NAME_MAX, ex.getMessage());
    }


    @Test
    void setEmail_ShouldSetValue_WhenValid() {
        String validEmail = "test@example.com";
        user.setEmail(validEmail);
        assertEquals(validEmail, user.getEmail());
    }

    @Test
    void setEmail_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setEmail(null));
        assertEquals(EMAIL_REQUIRED, ex.getMessage());
    }

    @Test
    void setEmail_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setEmail(" "));
        assertEquals(EMAIL_REQUIRED, ex.getMessage());
    }

    @Test
    void setEmail_ShouldThrowException_WhenInvalidFormat() {
        String invalidEmail = "invalid-email";
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setEmail(invalidEmail));
        assertEquals(EMAIL_INVALID, ex.getMessage());
    }


    @Test
    void setPassword_ShouldSetValue_WhenValid() {
        String validPassword = "password123";
        user.setPassword(validPassword);
        assertEquals(validPassword, user.getPassword());
    }

    @Test
    void setPassword_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setPassword(null));
        assertEquals(PASSWORD_REQUIRED, ex.getMessage());
    }

    @Test
    void setPassword_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setPassword(" "));
        assertEquals(PASSWORD_REQUIRED, ex.getMessage());
    }


    @Test
    void setPhone_ShouldSetValue_WhenValid() {
        String validPhone = "+1234567890";
        user.setPhone(validPhone);
        assertEquals(validPhone, user.getPhone());
    }

    @Test
    void setPhone_ShouldThrowException_WhenNull() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setPhone(null));
        assertEquals(PHONE_REQUIRED, ex.getMessage());
    }

    @Test
    void setPhone_ShouldThrowException_WhenBlank() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setPhone(" "));
        assertEquals(PHONE_REQUIRED, ex.getMessage());
    }

    @Test
    void setPhone_ShouldThrowException_WhenInvalidFormat() {
        String invalidPhone = "123456";
        BadRequestException ex = assertThrows(BadRequestException.class, () -> user.setPhone(invalidPhone));
        assertEquals(PHONE_FORMAT, ex.getMessage());
    }
}
