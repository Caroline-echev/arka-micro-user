package com.arka.micro_user.domain.model;

import static com.arka.micro_user.domain.util.constants.AddressConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AddressModelTest {

    @Test
    void testSetStreet_ValidValue_ShouldSetSuccessfully() {
        AddressModel address = new AddressModel();
        String validStreet = "Main Street";
        address.setStreet(validStreet);
        assertEquals(validStreet, address.getStreet());
    }

    @Test
    void testSetStreet_Null_ShouldThrowException() {
        AddressModel address = new AddressModel();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setStreet(null);
        });
        assertEquals(STREET_REQUIRED, exception.getMessage());
    }

    @Test
    void testSetStreet_Blank_ShouldThrowException() {
        AddressModel address = new AddressModel();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setStreet("  ");
        });
        assertEquals(STREET_REQUIRED, exception.getMessage());
    }

    @Test
    void testSetStreet_TooLong_ShouldThrowException() {
        AddressModel address = new AddressModel();
        String tooLongStreet = "a".repeat(STREET_MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setStreet(tooLongStreet);
        });
        assertEquals(STREET_MAX, exception.getMessage());
    }

    // Puedes replicar tests similares para city, country, nomenclature, state, observation y zipCode

    @Test
    void testSetObservation_Null_ShouldSetSuccessfully() {
        AddressModel address = new AddressModel();
        address.setObservation(null);
        assertNull(address.getObservation());
    }

    @Test
    void testSetObservation_TooLong_ShouldThrowException() {
        AddressModel address = new AddressModel();
        String tooLongObservation = "a".repeat(OBSERVATION_MAX_LENGTH + 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            address.setObservation(tooLongObservation);
        });
        assertEquals(OBSERVATION_MAX, exception.getMessage());
    }
}
