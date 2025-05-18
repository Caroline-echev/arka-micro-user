package com.arka.micro_user.domain.util;

public class UserConstants {

    private UserConstants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String ROLE_DOES_NOT_EXIST_EXCEPTION_MESSAGE = "Role does not exist";
    public static final String USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION_MESSAGE = "User already exists by email: ";
      public static final String USER_ALREADY_EXISTS_BY_DNI_EXCEPTION_MESSAGE = "User already exists by dni: ";
    public static final String INVALID_ROLE_EXCEPTION_MESSAGE =  "Invalid role name";

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
    public static final String REGEX_NUMERIC = "\\d+";
    public static final String REGEX_PHONE = "\\+\\d{1,15}";
    public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final int NAME_MAX_LENGTH = 60;

}
