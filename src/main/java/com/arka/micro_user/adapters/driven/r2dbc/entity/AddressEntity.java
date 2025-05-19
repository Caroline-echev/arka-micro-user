package com.arka.micro_user.adapters.driven.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tb_address")
public class AddressEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("street")
    private String street;

    @Column("city")
    private String city;

    @Column("country")
    private String country;

    @Column("nomenclature")
    private String nomenclature;

    @Column("state")
    private String state;

    @Column("observation")
    private String observation;

    @Column("zip_code")
    private String zipCode;
}
