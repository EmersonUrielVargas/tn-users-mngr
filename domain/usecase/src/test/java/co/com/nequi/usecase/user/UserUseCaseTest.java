package co.com.nequi.usecase.user;

import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserCacheGateway;
import co.com.nequi.model.user.gateways.UserExternalSourceGateway;
import co.com.nequi.model.user.gateways.UserNotificationGateway;
import co.com.nequi.model.user.gateways.UserPersistenceGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserPersistenceGateway userPersistenceGateway;

    @Mock
    private UserExternalSourceGateway userExternalSourceGateway;

    @Mock
    private UserCacheGateway userCacheGateway;

    @Mock
    private UserNotificationGateway userNotificationGateway;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userUseCase = new UserUseCase(userPersistenceGateway, userExternalSourceGateway, userCacheGateway, userNotificationGateway);
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

        @Test
        void should_return_external_user_data(){
            Long userId = 12L;
            User externalUser = User.builder()
                    .id(userId)
                    .email("test@email.com")
                    .name("Matilde")
                    .lastname("Rojas")
                    .build();

            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.empty());
            when(userExternalSourceGateway.findUser(userId)).thenReturn(Mono.just(externalUser));
            when(userPersistenceGateway.insertUser(externalUser)).thenReturn(Mono.just(externalUser));
            when(userNotificationGateway.notifyNewUser(externalUser)).thenReturn(Mono.empty());

            Mono<User> userReturn = userUseCase.createUser(userId);

            StepVerifier.create(userReturn)
                    .assertNext( user -> {
                        assert Objects.equals(user.getId(), externalUser.getId());
                        assert Objects.equals(user.getName(), externalUser.getName());
                    })
                    .verifyComplete();
            verify(userPersistenceGateway).findUserById(userId);
        }

        @Test
        void should_return_error_when_save_user_data(){
            Long userId = 12L;
            User externalUser = User.builder()
                    .id(userId)
                    .email("test@email.com")
                    .name("Matilde")
                    .lastname("Rojas")
                    .build();

            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.empty());
            when(userExternalSourceGateway.findUser(userId)).thenReturn(Mono.just(externalUser));
            when(userPersistenceGateway.insertUser(externalUser)).thenReturn(Mono.empty());

            Mono<User> userReturn = userUseCase.createUser(userId);

            StepVerifier.create(userReturn)
                    .expectErrorMatches( response -> {
                        Assertions.assertEquals(BusinessException.class, response.getClass());
                        BusinessException exception = (BusinessException) response;
                        Assertions.assertEquals("0-002", exception.getDomainMessage().getCode());
                        return true;
                    })
                    .verify();
            verify(userExternalSourceGateway).findUser(userId);
            verify(userNotificationGateway, never()).notifyNewUser(externalUser);
        }
    }

    @Nested
    @DisplayName("Find user by id tests")
    class FindUserByIdTest {
        @Test
        void should_return_user_from_cache() {
            Long userId = 1L;
            User cachedUser = User.builder()
                    .id(userId)
                    .name("Amanda")
                    .build();

            when(userCacheGateway.getUser(userId)).thenReturn(Mono.just(cachedUser));
            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.empty());

            Mono<User> result = userUseCase.findUserById(userId);

            StepVerifier.create(result)
                    .expectNextMatches(user -> user.getId().equals(userId)
                        && user.getName().equals("Amanda"))
                    .verifyComplete();
            verify(userCacheGateway).getUser(userId);
            verifyNoInteractions(userPersistenceGateway);
        }

        @Test
        void should_return_user_from_persistence_and_save_in_cache() {
            Long userId = 2L;
            User dbUser = User.builder()
                    .id(userId)
                    .name("Daniela")
                    .build();

            when(userCacheGateway.getUser(userId)).thenReturn(Mono.empty());
            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.just(dbUser));
            when(userCacheGateway.saveUser(dbUser)).thenReturn(Mono.just(dbUser));

            Mono<User> result = userUseCase.findUserById(userId);

            StepVerifier.create(result)
                    .expectNextMatches(user ->
                            user.getId().equals(userId)
                            && user.getName().equals("Daniela"))
                    .verifyComplete();
            verify(userPersistenceGateway).findUserById(userId);
            verify(userCacheGateway).saveUser(dbUser);
        }

        @Test
        void should_return_error_when_user_not_found() {
            Long userId = 3L;

            when(userCacheGateway.getUser(userId)).thenReturn(Mono.empty());
            when(userPersistenceGateway.findUserById(userId)).thenReturn(Mono.empty());

            Mono<User> result = userUseCase.findUserById(userId);

            StepVerifier.create(result)
                    .expectErrorMatches( response -> {
                        Assertions.assertEquals(BusinessException.class, response.getClass());
                        BusinessException exception = (BusinessException) response;
                        Assertions.assertEquals("404", exception.getDomainMessage().getCode());
                        return true;
                    })
                    .verify();
        }
    }

    @Nested
    @DisplayName("Find all users tests")
    class FindAllUsersTest {
        @Test
        void should_return_all_users() {
            User user1 = User.builder().id(1L).name("A").build();
            User user2 = User.builder().id(2L).name("B").build();

            when(userPersistenceGateway.findAllUser()).thenReturn(Flux.just(user1, user2));

            StepVerifier.create(userUseCase.findAllUser())
                    .expectNext(user1)
                    .expectNext(user2)
                    .verifyComplete();
        }
    }

    @Nested
    @DisplayName("Find all users by name tests")
    class FindAllUsersByNameTest {
        @Test
        void should_return_users_by_name() {
            String name = "Carlos";
            User user1 = User.builder()
                    .id(1L)
                    .name(name)
                    .build();

            when(userPersistenceGateway.findAllUsersByName(name)).thenReturn(Flux.just(user1));

            StepVerifier.create(userUseCase.findAllUsersByName(name))
                    .expectNext(user1)
                    .verifyComplete();
        }
    }

}
