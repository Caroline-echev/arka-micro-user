package com.arka.micro_user.domain.usecase;


import com.arka.micro_user.domain.api.IAuthServicePort;
import com.arka.micro_user.domain.exception.BadRequestException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.AuthenticationModel;
import com.arka.micro_user.domain.spi.IJwtPersistencePort;
import com.arka.micro_user.domain.spi.IPasswordEncoderPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IJwtPersistencePort jwtPersistencePort;


    @Override
    public Mono<String> authenticate(AuthenticationModel authModel) {
        return userPersistencePort.findByEmail(authModel.getEmail())
                .switchIfEmpty(Mono.error(new NotFoundException("Credenciales inválidas")))
                .flatMap(user -> {
                    boolean matches = passwordEncoderPersistencePort.matches(
                            authModel.getPassword(),
                            user.getPassword()
                    );

                    if (!matches) {
                        return Mono.error(new BadRequestException("Credenciales inválidas"));
                    }

                    return rolePersistencePort.getRoleById(user.getRoleId())
                            .switchIfEmpty(Mono.error(new NotFoundException("Role not found")))
                            .flatMap(role ->
                                    jwtPersistencePort.generateToken(
                                            user.getId().toString(),
                                            user.getEmail(),
                                            role.getName()
                                    )
                            );
                });
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return jwtPersistencePort.refreshToken(token);
    }

    @Override
    public Long getExpirationTime() {
        return jwtPersistencePort.getExpirationTime();
    }

}