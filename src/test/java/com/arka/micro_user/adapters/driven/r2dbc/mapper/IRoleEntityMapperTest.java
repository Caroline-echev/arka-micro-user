package com.arka.micro_user.adapters.driven.r2dbc.mapper;

import com.arka.micro_user.adapters.driven.r2dbc.entity.RoleEntity;
import com.arka.micro_user.data.RoleData;
import com.arka.micro_user.domain.model.RoleModel;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class IRoleEntityMapperTest {

    private final IRoleEntityMapper mapper = Mappers.getMapper(IRoleEntityMapper.class);

    @Test
    void toModel_shouldMapEntityToModelCorrectly() {
        RoleEntity entity = RoleData.getAdminRoleEntity();

        RoleModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(entity.getId());
        assertThat(model.getName()).isEqualTo(entity.getName());
        assertThat(model.getDescription()).isEqualTo(entity.getDescription());
    }
}
