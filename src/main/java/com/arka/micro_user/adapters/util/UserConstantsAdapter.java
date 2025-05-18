package com.arka.micro_user.adapters.util;

public class UserConstantsAdapter {

    private UserConstantsAdapter() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DNI_REQUIRED = "DNI is required";
    public static final String DNI_NUMERIC = "DNI must be numeric";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_MAX = "First name must not exceed 60 characters";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_MAX = "Last name must not exceed 60 characters";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Invalid email format";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PHONE_REQUIRED = "Phone number is required";
    public static final String PHONE_FORMAT = "Phone number must start with '+' and include country code (max 16 characters)";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";
    public static final String OLD_PASSWORD_REQUIRED = "Old password is required";
    public static final String REGEX_NUMERIC = "\\d+";
    public static final String REGEX_PHONE = "\\+\\d{1,15}";

    public static final int NAME_MAX_LENGTH = 60;
}
