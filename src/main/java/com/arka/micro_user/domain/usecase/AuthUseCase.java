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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.arka.micro_user.domain.util.constants.AuthConstants.INVALID_CREDENTIALS;
import static com.arka.micro_user.domain.util.constants.AuthConstants.ROLE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoderPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IJwtPersistencePort jwtPersistencePort;

    @Override
    public Mono<String> authenticate(AuthenticationModel authModel) {
        log.info("Starting authentication for email: {}", authModel.getEmail());
        return userPersistencePort.findByEmail(authModel.getEmail())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Invalid credentials: user with email {} not found", authModel.getEmail());
                    return Mono.error(new NotFoundException(INVALID_CREDENTIALS));
                }))
                .flatMap(user -> {
                    boolean matches = passwordEncoderPersistencePort.matches(
                            authModel.getPassword(),
                            user.getPassword()
                    );

                    if (!matches) {
                        log.warn("Invalid credentials: password does not match for user {}", authModel.getEmail());
                        return Mono.error(new BadRequestException(INVALID_CREDENTIALS));
                    }

                    log.debug("Password matches for user: {}", authModel.getEmail());

                    return rolePersistencePort.getRoleById(user.getRoleId())
                            .switchIfEmpty(Mono.defer(() -> {
                                log.error("Role not found for user ID={}", user.getId());
                                return Mono.error(new NotFoundException(ROLE_NOT_FOUND));
                            }))
                            .flatMap(role -> {
                                log.info("Generating token for user {} with role {}", authModel.getEmail(), role.getName());
                                return jwtPersistencePort.generateToken(
                                        user.getId().toString(),
                                        user.getEmail(),
                                        role.getName()
                                );
                            });
                });
    }

    @Override
    public Mono<String> refreshToken(String token) {
        log.info("Requesting token refresh");
        return jwtPersistencePort.refreshToken(token)
                .doOnSuccess(newToken -> log.debug("Token refreshed successfully"));
    }

    @Override
    public Long getExpirationTime() {
        Long expiration = jwtPersistencePort.getExpirationTime();
        log.debug("Retrieved token expiration time: {} ms", expiration);
        return expiration;
    }

}
