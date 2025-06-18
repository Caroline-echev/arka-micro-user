package com.arka.micro_user.adapters.driving.reactive.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserResponse {


        private Long id;
        private String dni;
        private String firstName ;
        private String lastName;
        private String email;
        private String password;
        private String phone;
        private String status;
        private Long roleId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
