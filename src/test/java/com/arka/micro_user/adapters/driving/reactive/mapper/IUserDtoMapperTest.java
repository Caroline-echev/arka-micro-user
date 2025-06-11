package com.arka.micro_user.adapters.driving.reactive.mapper;

import com.arka.micro_user.adapters.driving.reactive.dto.request.UserRequest;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.model.UserModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class IUserDtoMapperTest {

    private final IUserDtoMapper mapper = Mappers.getMapper(IUserDtoMapper.class);

    @Test
    void toModel_shouldMapFieldsCorrectlyAndIgnoreRoleId() {

        UserRequest request = UserData.getAdminUserRequest();

        UserModel model = mapper.toModel(request);

        assertThat(model.getDni()).isEqualTo(request.getDni());
        assertThat(model.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(model.getLastName()).isEqualTo(request.getLastName());
        assertThat(model.getEmail()).isEqualTo(request.getEmail());
        assertThat(model.getPassword()).isEqualTo(request.getPassword());
        assertThat(model.getPhone()).isEqualTo(request.getPhone());
        assertThat(model.getRoleId()).isNull();
    }
}
