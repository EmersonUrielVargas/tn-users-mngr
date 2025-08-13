package co.com.nequi.usecase.user;

import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserExternalSourceGateway;
import co.com.nequi.model.user.gateways.UserPersistenceGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserPersistenceGateway userPersistenceGateway;

    @Mock
    private UserExternalSourceGateway userExternalSourceGateway;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userUseCase = new UserUseCase(userPersistenceGateway, userExternalSourceGateway);
    }

    @Nested
    @DisplayName("Create user tests")
    class CreateUserTest {
        @Test
        void should_return_bd_user_data(){
            Long userId = 12L;
            User userBd = User.builder()
                    .id(userId)
                    .email("test@email.com")
                    .name("Cesar")
                    .lastname("restrepo")
                    .build();
            User externalUser = User.builder()
                    .id(userId)
                    .email("test@email.com")
                    .name("Matilde")
                    .lastname("Rojas")
                    .build();

            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.just(userBd));
            when(userExternalSourceGateway.findUser(userId)).thenReturn(Mono.just(externalUser));

            Mono<User> userReturn = userUseCase.createUser(userId);

            StepVerifier.create(userReturn)
                    .assertNext( user -> {
                        assert Objects.equals(user.getId(), userBd.getId());
                        assert Objects.equals(user.getName(), userBd.getName());
                    })
                    .verifyComplete();
            verify(userPersistenceGateway).findUserById(userId);
        }
    }

}
