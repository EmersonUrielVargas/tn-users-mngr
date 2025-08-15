package co.com.nequi.redis.template;

import co.com.nequi.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class UserRedisAdapterOperationsTest {

    @Mock
    private ReactiveRedisConnectionFactory connectionFactory;

    @Mock
    private ObjectMapper objectMapper;

    private UserRedisAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(objectMapper.map("value", Object.class)).thenReturn("value");

        adapter = new UserRedisAdapter(connectionFactory, objectMapper);
    }

    @Test
    void testSave() {
        StepVerifier.create(adapter.save("key", User.builder().build()))
                .expectNext(User.builder().build())
                .verifyComplete();
    }

    @Test
    void testSaveWithExpiration() {

        StepVerifier.create(adapter.save("key", User.builder().build(), 2))
                .expectNext(User.builder().build())
                .verifyComplete();
    }

    @Test
    void testFindById() {

        StepVerifier.create(adapter.findById("key"))
                .verifyComplete();
    }

}