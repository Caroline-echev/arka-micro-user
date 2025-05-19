package com.arka.micro_user.domain.model;

import lombok.Data;

import static com.arka.micro_user.domain.util.constants.AddressConstants.*;


@Data
public class AddressModel {

    private Long id;
    private Long userId;
    private String street;
    private String city;
    private String country;
    private String nomenclature;
    private String state;
    private String observation;
    private String zipCode;

    public void setStreet(String street) {
        if (street == null || street.isBlank()) throw new IllegalArgumentException(STREET_REQUIRED);
        if (street.length() > STREET_MAX_LENGTH) throw new IllegalArgumentException(STREET_MAX);
        this.street = street;
    }

    public void setCity(String city) {
        if (city == null || city.isBlank()) throw new IllegalArgumentException(CITY_REQUIRED);
        if (city.length() > CITY_MAX_LENGTH) throw new IllegalArgumentException(CITY_MAX);
        this.city = city;
    }

    public void setCountry(String country) {
        if (country == null || country.isBlank()) throw new IllegalArgumentException(COUNTRY_REQUIRED);
        if (country.length() > COUNTRY_MAX_LENGTH) throw new IllegalArgumentException(COUNTRY_MAX);
        this.country = country;
    }

    public void setNomenclature(String nomenclature) {
        if (nomenclature == null || nomenclature.isBlank()) throw new IllegalArgumentException(NOMENCLATURE_REQUIRED);
        if (nomenclature.length() > NOMENCLATURE_MAX_LENGTH) throw new IllegalArgumentException(NOMENCLATURE_MAX);
        this.nomenclature = nomenclature;
    }

    public void setState(String state) {
        if (state == null || state.isBlank()) throw new IllegalArgumentException(STATE_REQUIRED);
        if (state.length() > STATE_MAX_LENGTH) throw new IllegalArgumentException(STATE_MAX);
        this.state = state;
    }

    public void setObservation(String observation) {
        if (observation != null && observation.length() > OBSERVATION_MAX_LENGTH)
            throw new IllegalArgumentException(OBSERVATION_MAX);
        this.observation = observation;
    }

    public void setZipCode(String zipCode) {
        if (zipCode == null || zipCode.isBlank()) throw new IllegalArgumentException(ZIP_CODE_REQUIRED);
        if (zipCode.length() > ZIP_CODE_MAX_LENGTH) throw new IllegalArgumentException(ZIP_CODE_MAX);
        this.zipCode = zipCode;
    }
}
