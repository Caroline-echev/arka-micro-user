package com.arka.micro_user.adapters.driven.r2dbc.adapter;

import com.arka.micro_user.adapters.driven.r2dbc.mapper.IUserEntityMapper;
import com.arka.micro_user.adapters.driven.r2dbc.repository.IUserRepository;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.adapters.driven.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userMapper;

    @InjectMocks
    private UserAdapter userAdapter;

    private UserModel userModel;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userModel = UserData.getAdminUser();

        userEntity = UserData.getAdminUserEntity();
    }

    @Test
    void saveUser_ShouldReturnSavedUserModel() {
        when(userMapper.toEntity(userModel)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(userModel);

        Mono<UserModel> result = userAdapter.saveUser(userModel);

        StepVerifier.create(result)
                .expectNext(userModel)
                .verifyComplete();

        verify(userMapper).toEntity(userModel);
        verify(userRepository).save(userEntity);
        verify(userMapper).toModel(userEntity);
    }

    @Test
    void findByEmail_ShouldReturnUserModel_WhenUserExists() {
        String email = userModel.getEmail();

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(userModel);

        Mono<UserModel> result = userAdapter.findByEmail(email);

        StepVerifier.create(result)
                .expectNext(userModel)
                .verifyComplete();

        verify(userRepository).findByEmail(email);
        verify(userMapper).toModel(userEntity);
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenUserDoesNotExist() {
        String email = "notfound@arka.com";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        Mono<UserModel> result = userAdapter.findByEmail(email);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository).findByEmail(email);
        verify(userMapper, never()).toModel(any());
    }

    @Test
    void findByDni_ShouldReturnUserModel_WhenUserExists() {
        String dni = userModel.getDni();

        when(userRepository.findByDni(dni)).thenReturn(Mono.just(userEntity));
        when(userMapper.toModel(userEntity)).thenReturn(userModel);

        Mono<UserModel> result = userAdapter.findByDni(dni);

        StepVerifier.create(result)
                .expectNext(userModel)
                .verifyComplete();

        verify(userRepository).findByDni(dni);
        verify(userMapper).toModel(userEntity);
    }

    @Test
    void findByDni_ShouldReturnEmpty_WhenUserDoesNotExist() {
        String dni = "00000000";

        when(userRepository.findByDni(dni)).thenReturn(Mono.empty());

        Mono<UserModel> result = userAdapter.findByDni(dni);

        StepVerifier.create(result)
                .verifyComplete();

        verify(userRepository).findByDni(dni);
        verify(userMapper, never()).toModel(any());
    }
}
