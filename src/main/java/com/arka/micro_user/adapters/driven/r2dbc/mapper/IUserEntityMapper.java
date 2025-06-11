package com.arka.micro_user.adapters.driven.r2dbc.mapper;


import com.arka.micro_user.adapters.driven.r2dbc.entity.UserEntity;
import com.arka.micro_user.domain.model.UserModel;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserEntity toEntity(UserModel userModel);

    UserModel toModel(UserEntity userEntity);
}
