package com.arka.micro_user.adapters.driven.r2dbc.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsR2DBCTest {

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        Constructor<ConstantsR2DBC> constructor = ConstantsR2DBC.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            fail("Expected an IllegalStateException to be thrown");
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalStateException);
            assertEquals("Utility class", cause.getMessage());
        }
    }
}
