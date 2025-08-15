package co.com.nequi.r2dbc;

import co.com.nequi.model.user.User;
import co.com.nequi.r2dbc.adapter.UserRepositoryAdapter;
import co.com.nequi.r2dbc.entity.UserEntity;
import co.com.nequi.r2dbc.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    UserRepositoryAdapter userRepositoryAdapter;

    @Mock
    UserRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        UserEntity userBd = UserEntity.builder()
                .id(12L)
                .name("Lu")
                .build();

        User userDomain = User.builder()
                .id(12L)
                .name("Lu")
                .build();

        when(repository.findById(12L)).thenReturn(Mono.just(userBd));
        when(mapper.map(userBd, User.class)).thenReturn(userDomain);

        Mono<User> result = userRepositoryAdapter.findUserById(12L);

        StepVerifier.create(result)
                .expectNextMatches(userFoundBd -> {
                    Assertions.assertEquals(userFoundBd.getId(), userDomain.getId());
                    Assertions.assertEquals(userFoundBd.getName(), userDomain.getName());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        UserEntity userBd = UserEntity.builder()
                .id(12L)
                .name("Beatriz")
                .build();

        User userDomain = User.builder()
                .id(12L)
                .name("Beatriz")
                .build();

        when(repository.findAll()).thenReturn(Flux.just(userBd));
        when(mapper.map(userBd, User.class)).thenReturn(userDomain);

        Flux<User> result = userRepositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(userFoundBd -> {
                    Assertions.assertEquals(userFoundBd.getId(), userDomain.getId());
                    Assertions.assertEquals(userFoundBd.getName(), userDomain.getName());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {

        UserEntity userBd = UserEntity.builder()
                .id(12L)
                .name("Monica")
                .build();

        User userDomain = User.builder()
                .id(12L)
                .name("Monica")
                .build();
        when(repository.save(userBd)).thenReturn(Mono.just(userBd));
        when(mapper.map(userDomain, UserEntity.class)).thenReturn(userBd);
        when(mapper.map(userBd, User.class)).thenReturn(userDomain);

        Mono<User> result = userRepositoryAdapter.save(userDomain);

        StepVerifier.create(result)
                .expectNextMatches(userFoundBd -> {
                    Assertions.assertEquals(userFoundBd.getId(), userDomain.getId());
                    Assertions.assertEquals(userFoundBd.getName(), userDomain.getName());
                    return true;
                })
                .verifyComplete();
    }
}
