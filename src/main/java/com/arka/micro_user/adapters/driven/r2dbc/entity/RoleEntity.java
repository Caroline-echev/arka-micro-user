package com.arka.micro_user.adapters.driven.r2dbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("tb_role")
public class RoleEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

}
