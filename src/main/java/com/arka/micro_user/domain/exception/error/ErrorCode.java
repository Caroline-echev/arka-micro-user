package com.arka.micro_user.domain.exception.error;

public interface ErrorCode {
    String getCode();
    
    String getMessage();

    int getStatusCode();

    ErrorCategory getCategory();
}