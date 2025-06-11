package com.arka.micro_user.adapters.driven.r2dbc.mapper;


import com.arka.micro_user.adapters.driven.r2dbc.entity.RoleEntity;
import com.arka.micro_user.domain.model.RoleModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleEntityMapper {

    RoleModel toModel(RoleEntity roleEntity);
}
