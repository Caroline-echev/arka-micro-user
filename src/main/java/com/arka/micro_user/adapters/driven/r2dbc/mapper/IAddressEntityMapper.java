package com.arka.micro_user.adapters.driven.r2dbc.mapper;


import com.arka.micro_user.adapters.driven.r2dbc.entity.AddressEntity;
import com.arka.micro_user.domain.model.AddressModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAddressEntityMapper {
 AddressModel toModel(AddressEntity addressEntity);
 AddressEntity toEntity(AddressModel addressModel);
}
