package com.arka.micro_user.data;

import com.arka.micro_user.adapters.driven.r2dbc.entity.RoleEntity;
import com.arka.micro_user.domain.model.RoleModel;

public class RoleData {

    public static RoleModel getAdminRole() {
        return RoleModel.builder()
                .id(1L)
                .name("ADMIN")
                .description("System Administrator")
                .build();
    }
    public static RoleEntity getAdminRoleEntity() {
        return RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .description("System Administrator")
                .build();
    }

    public static RoleModel getLogisticRole() {
        return RoleModel.builder()
                .id(2L)
                .name("LOGISTIC")
                .description("Logistics role")
                .build();
    }

    public static RoleModel getClientRole() {
        return RoleModel.builder()
                .id(3L)
                .name("CLIENT")
                .description("Customer role")
                .build();
    }
}
