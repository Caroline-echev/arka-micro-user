package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IRoleEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IRoleRepository;
import com.arka.micro_user.data.RoleData;
import com.arka.micro_user.domain.model.RoleModel;
import com.arka.micro_user.adapters.driven.r2dbc.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RoleAdapterTest {

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRoleEntityMapper roleMapper;

    @InjectMocks
    private RoleAdapter roleAdapter;

    private RoleModel roleModel;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleModel = RoleData.getAdminRole();

        roleEntity = RoleData.getAdminRoleEntity();
    }

    @Test
    void getRoleByName_ShouldReturnRoleModel_WhenRoleExists() {
        String roleName = roleModel.getName();

        when(roleRepository.findByName(roleName)).thenReturn(Mono.just(roleEntity));
        when(roleMapper.toModel(roleEntity)).thenReturn(roleModel);

        Mono<RoleModel> result = roleAdapter.getRoleByName(roleName);

        StepVerifier.create(result)
                .expectNext(roleModel)
                .verifyComplete();

        verify(roleRepository).findByName(roleName);
        verify(roleMapper).toModel(roleEntity);
    }

    @Test
    void getRoleByName_ShouldReturnEmpty_WhenRoleDoesNotExist() {
        String roleName = "NON_EXISTENT_ROLE";

        when(roleRepository.findByName(roleName)).thenReturn(Mono.empty());

        Mono<RoleModel> result = roleAdapter.getRoleByName(roleName);

        StepVerifier.create(result)
                .verifyComplete();

        verify(roleRepository).findByName(roleName);
        verify(roleMapper, never()).toModel(any());
    }
}
