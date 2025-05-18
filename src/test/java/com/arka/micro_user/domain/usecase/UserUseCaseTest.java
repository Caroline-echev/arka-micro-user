package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.data.RoleData;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.enums.UserRole;
import com.arka.micro_user.domain.exception.BadRequestException;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.RoleModel;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import com.arka.micro_user.domain.util.UserValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.arka.micro_user.domain.util.UserConstants.USER_ALREADY_EXISTS_BY_DNI_EXCEPTION_MESSAGE;
import static com.arka.micro_user.domain.util.UserConstants.USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION_MESSAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPersistencePort passwordEncoderPersistencePort;

    @Mock
    private IRolePersistencePort rolePersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserAdminLogistic_Success_Admin() {
        UserModel user = UserData.getAdminUser();
        RoleModel role = RoleData.getAdminRole();

        when(rolePersistencePort.getRoleByName(UserRole.ADMIN.name())).thenReturn(Mono.just(role));
        when(userPersistencePort.findByEmail(user.getEmail())).thenReturn(Mono.empty());
        when(userPersistencePort.findByDni(user.getDni())).thenReturn(Mono.empty());
        when(passwordEncoderPersistencePort.encodePassword(user.getPassword())).thenReturn(Mono.just("hashed-password"));
        when(userPersistencePort.saveUser(any(UserModel.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.createUserAdminLogistic(user, UserRole.ADMIN.name()))
                .expectNextMatches(result -> result.getRoleId().equals(role.getId()) && result.getPassword().equals("hashed-password"))
                .verifyComplete();

        verify(userPersistencePort).saveUser(any(UserModel.class));
    }

    @Test
    void createUserAdminLogistic_InvalidRole() {
        UserModel user = UserData.getAdminUser();

        StepVerifier.create(userUseCase.createUserAdminLogistic(user, "UNKNOWN"))
                .expectError(BadRequestException.class)
                .verify();

        verify(rolePersistencePort, never()).getRoleByName(any());
    }

    @Test
    void createUserAdminLogistic_RoleNotFound() {
        UserModel user = UserData.getAdminUser();

        when(rolePersistencePort.getRoleByName(UserRole.ADMIN.name())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createUserAdminLogistic(user, UserRole.ADMIN.name()))
                .expectError(NotFoundException.class)
                .verify();
    }


    @Test
    void validateUserDoesNotExistByEmail_UserExists_ShouldThrowException() {

        UserModel user = UserData.getAdminUser();
        String email = user.getEmail();
        when(userPersistencePort.findByEmail(email)).thenReturn(Mono.just(user));

        StepVerifier.create(UserValidationUtil.validateUserDoesNotExistByEmail(email, userPersistencePort))
                .expectErrorMatches(throwable -> throwable instanceof DuplicateResourceException &&
                        throwable.getMessage().contains(USER_ALREADY_EXISTS_BY_EMAIL_EXCEPTION_MESSAGE + email))
                .verify();
        verify(userPersistencePort).findByEmail(email);
    }
    @Test
    void validateUserDoesNotExistByDni_UserNotExists_ShouldThrowException() {
        UserModel user = UserData.getAdminUser();
        String dni = user.getDni();

        when(userPersistencePort.findByDni(dni)).thenReturn(Mono.justOrEmpty(user));
        StepVerifier.create(UserValidationUtil.validateUserDoesNotExistByDni(dni, userPersistencePort))
                .expectErrorMatches(throwable -> throwable instanceof DuplicateResourceException &&
                        throwable.getMessage().contains(USER_ALREADY_EXISTS_BY_DNI_EXCEPTION_MESSAGE + dni))
                .verify();

        verify(userPersistencePort).findByDni(dni);
    }

}
