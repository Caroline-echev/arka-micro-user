package com.arka.micro_user.adapters.util;

public class AddressConstantsAdapter {

    private AddressConstantsAdapter() {
        throw new IllegalStateException("Utility class");
    }
    public static final String STREET_REQUIRED = "Street is required";
    public static final String STREET_MAX = "Street must not exceed 20 characters";
    public static final String CITY_REQUIRED = "City is required";
    public static final String CITY_MAX = "City must not exceed 60 characters";
    public static final String COUNTRY_REQUIRED = "Country is required";
    public static final String COUNTRY_MAX = "Country must not exceed 60 characters";
    public static final String NOMENCLATURE_REQUIRED = "Nomenclature is required";
    public static final String NOMENCLATURE_MAX = "Nomenclature must not exceed 60 characters";
    public static final String STATE_REQUIRED = "State is required";
    public static final String STATE_MAX = "State must not exceed 60 characters";
    public static final String OBSERVATION_MAX = "Observation must not exceed 500 characters";
    public static final String ZIP_CODE_REQUIRED = "Zip code is required";
    public static final String ZIP_CODE_MAX = "Zip code must not exceed 20 characters";

    public static final int STREET_MAX_LENGTH = 20;
    public static final int CITY_MAX_LENGTH = 500;
    public static final int ZIP_CODE_MAX_LENGTH = 20;
    public static final int NOMENCLATURE_MAX_LENGTH = 60;
    public static final int STATE_MAX_LENGTH = 60;
    public static final int OBSERVATION_MAX_LENGTH = 500;
    public static final int COUNTRY_MAX_LENGTH = 60;
}
