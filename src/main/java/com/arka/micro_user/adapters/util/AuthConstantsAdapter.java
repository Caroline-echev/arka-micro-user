package com.arka.micro_user.adapters.util;

public class AuthConstantsAdapter {
    private AuthConstantsAdapter() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BEARER = "Bearer";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required.";
}
