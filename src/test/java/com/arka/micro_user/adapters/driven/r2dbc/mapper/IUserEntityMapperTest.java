package com.arka.micro_user.adapters.driven.r2dbc.mapper;

import com.arka.micro_user.adapters.driven.r2dbc.entity.UserEntity;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.model.UserModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class IUserEntityMapperTest {

    private final IUserEntityMapper mapper = Mappers.getMapper(IUserEntityMapper.class);

    @Test
    void toModel_shouldMapEntityToModelCorrectly() {
        UserEntity entity = UserData.getAdminUserEntity();

        UserModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(entity.getId());
        assertThat(model.getDni()).isEqualTo(entity.getDni());
        assertThat(model.getFirstName()).isEqualTo(entity.getFirstName());
        assertThat(model.getLastName()).isEqualTo(entity.getLastName());
        assertThat(model.getEmail()).isEqualTo(entity.getEmail());
        assertThat(model.getPassword()).isEqualTo(entity.getPassword());
        assertThat(model.getPhone()).isEqualTo(entity.getPhone());
        assertThat(model.getStatus()).isEqualTo(entity.getStatus());
        assertThat(model.getRoleId()).isEqualTo(entity.getRoleId());
        assertThat(model.getCreatedAt()).isEqualTo(entity.getCreatedAt());
        assertThat(model.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
    }

    @Test
    void toEntity_shouldMapModelToEntityCorrectly() {
        UserModel model = UserData.getAdminUser();

        UserEntity entity = mapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(model.getId());
        assertThat(entity.getDni()).isEqualTo(model.getDni());
        assertThat(entity.getFirstName()).isEqualTo(model.getFirstName());
        assertThat(entity.getLastName()).isEqualTo(model.getLastName());
        assertThat(entity.getEmail()).isEqualTo(model.getEmail());
        assertThat(entity.getPassword()).isEqualTo(model.getPassword());
        assertThat(entity.getPhone()).isEqualTo(model.getPhone());
        assertThat(entity.getStatus()).isEqualTo(model.getStatus());
        assertThat(entity.getRoleId()).isEqualTo(model.getRoleId());
        assertThat(entity.getCreatedAt()).isEqualTo(model.getCreatedAt());
        assertThat(entity.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    }
}
