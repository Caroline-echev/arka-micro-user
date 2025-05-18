package com.arka.micro_user.adapters.driving.reactive.mapper;

import com.arka.micro_user.adapters.driving.reactive.dto.request.UserRequest;
import com.arka.micro_user.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface IUserDtoMapper {
    @Mapping(target = "roleId", ignore = true)
    UserModel toModel(UserRequest userRequest);
}

