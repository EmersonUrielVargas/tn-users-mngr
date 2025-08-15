package co.com.nequi.consumer;


import co.com.nequi.consumer.user.UserAdapter;
import co.com.nequi.consumer.user.dto.response.APISuccessResponse;
import co.com.nequi.consumer.user.dto.response.UserData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.test.StepVerifier;
import java.io.IOException;
import java.time.Duration;


class UserRestAdapterTest {

    private static UserAdapter userAdapter;

    private static MockWebServer mockBackEnd;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {

        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        objectMapper = new ObjectMapper();

        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(1000));

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        userAdapter = new UserAdapter(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function Get users by Id.")
    void validateGetUserById() throws JsonProcessingException {


        UserData user =  UserData.builder()
                .id(12L)
                .first_name("Emmanuel")
                .last_name("casas")
                .avatar("url")
                .email("email")
                .build();
        APISuccessResponse responseBody = APISuccessResponse.builder().data(user).build();
        String jsonBody = objectMapper.writeValueAsString(responseBody);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody(jsonBody));
        var response = userAdapter.findUser(12L);

        StepVerifier.create(response)
                .expectNextMatches(objectResponse -> objectResponse.getId().equals(12L))
                .verifyComplete();
    }
}