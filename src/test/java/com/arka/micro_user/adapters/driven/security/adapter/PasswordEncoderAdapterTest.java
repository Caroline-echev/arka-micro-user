package com.arka.micro_user.adapters.driven.security.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderAdapterTest {

    private PasswordEncoderAdapter passwordEncoderAdapter;

    @BeforeEach
    void setUp() {
        passwordEncoderAdapter = new PasswordEncoderAdapter();
    }

    @Test
    void shouldEncodePassword() {
        String rawPassword = "mySecret123";

        StepVerifier.create(passwordEncoderAdapter.encodePassword(rawPassword))
                .assertNext(encodedPassword -> {
                    assertNotNull(encodedPassword);
                    assertNotEquals(rawPassword, encodedPassword); // Debe estar codificado
                    assertTrue(passwordEncoderAdapter.matches(rawPassword, encodedPassword)); // Debe coincidir
                })
                .verifyComplete();
    }

    @Test
    void shouldMatchEncodedPassword() {
        String rawPassword = "anotherSecret";
        String encodedPassword = passwordEncoderAdapter.encodePassword(rawPassword).block();

        assertNotNull(encodedPassword);
        assertTrue(passwordEncoderAdapter.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldNotMatchWrongPassword() {
        String rawPassword = "password1";
        String encodedPassword = passwordEncoderAdapter.encodePassword("differentPassword").block();

        assertNotNull(encodedPassword);
        assertFalse(passwordEncoderAdapter.matches(rawPassword, encodedPassword));
    }
}
