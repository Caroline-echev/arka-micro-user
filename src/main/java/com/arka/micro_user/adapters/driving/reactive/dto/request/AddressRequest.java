package com.arka.micro_user.adapters.driving.reactive.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.arka.micro_user.adapters.util.AddressConstantsAdapter.*;

@Data
public class AddressRequest {

    @NotBlank(message = STREET_REQUIRED)
    @Size(max = STREET_MAX_LENGTH, message = STREET_MAX)
    private String street;

    @NotBlank(message = CITY_REQUIRED)
    @Size(max = CITY_MAX_LENGTH, message = CITY_MAX)
    private String city;

    @NotBlank(message = COUNTRY_REQUIRED)
    @Size(max = COUNTRY_MAX_LENGTH, message = COUNTRY_MAX)
    private String country;

    @NotBlank(message = NOMENCLATURE_REQUIRED)
    @Size(max = NOMENCLATURE_MAX_LENGTH, message = NOMENCLATURE_MAX)
    private String nomenclature;

    @NotBlank(message = STATE_REQUIRED)
    @Size(max = STATE_MAX_LENGTH, message = STATE_MAX)
    private String state;

    @Size(max = OBSERVATION_MAX_LENGTH, message = OBSERVATION_MAX)
    private String observation;

    @NotBlank(message = ZIP_CODE_REQUIRED)
    @Size(max = ZIP_CODE_MAX_LENGTH, message = ZIP_CODE_MAX)
    private String zipCode;
}
