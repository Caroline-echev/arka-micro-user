package com.arka.micro_user.adapters.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class UserConstantsAdapterTest {

    @Test
    void testPrivateConstructorThrowsException() throws Exception {
        Constructor<UserConstantsAdapter> constructor = UserConstantsAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(thrown.getCause() instanceof IllegalStateException);
        assertEquals("Utility class", thrown.getCause().getMessage());
    }
}
