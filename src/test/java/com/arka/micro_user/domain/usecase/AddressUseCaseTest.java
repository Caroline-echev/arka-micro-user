package com.arka.micro_user.domain.usecase;

import com.arka.micro_user.data.AddressData;
import com.arka.micro_user.data.RoleData;
import com.arka.micro_user.data.UserData;
import com.arka.micro_user.domain.exception.DuplicateResourceException;
import com.arka.micro_user.domain.exception.NotFoundException;
import com.arka.micro_user.domain.model.AddressModel;
import com.arka.micro_user.domain.model.UserModel;
import com.arka.micro_user.domain.spi.IAddressPersistencePort;
import com.arka.micro_user.domain.spi.IRolePersistencePort;
import com.arka.micro_user.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressUseCaseTest {

}
