package com.arka.micro_user.adapters.driving.reactive.mapper;

import com.arka.micro_user.adapters.driving.reactive.dto.request.AddressRequest;
import com.arka.micro_user.domain.model.AddressModel;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface IAddressDtoMapper {
    AddressModel toModel(AddressRequest request);
}

