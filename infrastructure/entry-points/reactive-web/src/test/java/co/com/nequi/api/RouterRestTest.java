package co.com.nequi.api;

import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.api.dto.response.UserDetails;
import co.com.nequi.api.handler.UserHandler;
import co.com.nequi.api.routers.RouterRest;
import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.exceptions.TechnicalException;
import co.com.nequi.model.user.User;
import co.com.nequi.usecase.user.UserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, UserHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    private final String basePath = "/api/v1/users";

    @Nested
    @DisplayName("POST /api/v1/users/{id}")
    class createUserTests {
        private final String pathTest ="/{id}";

        @Test
        void testCreateUserSuccessful() throws Exception {
            Long userId = 12L;
            User user = buildUser();
            String jsonBody = objectMapper.writeValueAsString(user);

            when(userUseCase.createUser(anyLong())).thenReturn(Mono.just(user));

            webTestClient.post()
                    .uri(basePath.concat(pathTest), userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(UserDetails.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse.getEmail()).isEqualTo(user.getEmail());
                            }
                    );
        }

        @Test
        void createUserErrorTest() {
            Long userId = 12L;

            when(userUseCase.createUser(any()))
                    .thenReturn(Mono.error(new BusinessException(DomainMessage.USER_CREATION_FAIL)));

            webTestClient.post()
                    .uri(basePath.concat(pathTest), userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .is4xxClientError();
        }

    }

    @Nested
    @DisplayName("GET /api/v1/users/{id}")
    class getUserByIdTests {
        private final String pathTest ="/{id}";

        @Test
        void testGetUserByIdSuccessful() {
            Long userId = 12L;
            User user = buildUser();

            when(userUseCase.findUserById(anyLong())).thenReturn(Mono.just(user));

            webTestClient.get()
                    .uri(basePath.concat(pathTest), userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDetails.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse.getEmail()).isEqualTo(user.getEmail());
                                Assertions.assertThat(bodyResponse.getLastname()).isEqualTo(user.getLastname());
                            }
                    );
        }

        @Test
        void testGetUserByIdErrorTest() {
            Long userId = 12L;

            when(userUseCase.findUserById(any()))
                    .thenReturn(Mono.error(new BusinessException(DomainMessage.USER_NOT_FOUND)));

            webTestClient.get()
                    .uri(basePath.concat(pathTest), userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isNotFound();
        }

    }

    @Nested
    @DisplayName("GET /api/v1/users")
    class getAllUserTests {

        @Test
        void getAllUsersSuccessful() {
            Long userId = 12L;
            User user = buildUser();

            when(userUseCase.findAllUser()).thenReturn(Flux.just(user));

            webTestClient.get()
                    .uri(basePath, userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(UserDetails.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse).hasSize(1);
                                Assertions.assertThat(bodyResponse.get(0).getLastname()).isEqualTo(user.getLastname());
                            }
                    );
        }

        @Test
        void testGetAllUsersError() {

            when(userUseCase.findAllUser())
                    .thenReturn(Flux.error(new TechnicalException(DomainMessage.ERROR_USER_ADAPTER)));

            webTestClient.get()
                    .uri(basePath)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .is5xxServerError();
        }

    }

    @Nested
    @DisplayName("GET /api/v1/users?name={name}")
    class getUsersFilterNameTests {

        @Test
        void getUsersFilterNameSuccessful() {
            String nameFilter = "ken";

            User user = buildUser();
            User user1 = buildUser();
            user1.setId(1L);

            when(userUseCase.findAllUsersByName(anyString())).thenReturn(Flux.just(user, user1));

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(basePath)
                            .queryParam(CommonConstants.QUERY_PARAM_FILTER_NAME, nameFilter)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(UserDetails.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse).hasSize(2);
                                Assertions.assertThat(bodyResponse.get(1).getId()).isEqualTo(user1.getId());
                            }
                    );
        }

        @Test
        void getUsersFilterNameError() {
            String nameFilter = "ken";

            when(userUseCase.findAllUsersByName(nameFilter))
                    .thenReturn(Flux.error(new TechnicalException(DomainMessage.ERROR_USER_ADAPTER)));

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(basePath)
                            .queryParam(CommonConstants.QUERY_PARAM_FILTER_NAME, nameFilter)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .is5xxServerError();
        }

    }


    private User buildUser(){
        return User.builder()
                .id(12L)
                .name("Emmanuel")
                .lastname("casas")
                .email("test@test.com")
                .photoUrl("https://photo.url")
                .build();
    }
}
