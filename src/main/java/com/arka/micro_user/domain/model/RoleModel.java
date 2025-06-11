package com.arka.micro_user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RoleModel {

    private Long id;
    private String name;
    private String description;
}
