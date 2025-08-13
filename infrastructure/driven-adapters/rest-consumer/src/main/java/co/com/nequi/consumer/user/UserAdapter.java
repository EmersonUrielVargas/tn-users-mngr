package co.com.nequi.consumer.user;

import co.com.nequi.consumer.constants.RestConstants;
import co.com.nequi.consumer.user.dto.response.APISuccessResponse;
import co.com.nequi.consumer.user.mapper.UserMapper;
import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.exceptions.TechnicalException;
import co.com.nequi.model.exceptions.UserException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserExternalSourceGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class UserAdapter implements UserExternalSourceGateway {

    private final WebClient client;

    @Override
    public Mono<User> findUser(Long id) {
        return Mono.defer(() -> client.get()
                .uri(RestConstants.PATH_ID_PARAM, id)
                .accept(MediaType.APPLICATION_JSON)
                .header(RestConstants.HEADER_API_KEY, "reqres-free-v1")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, DomainMessage.ERROR_DATA_USER_ADAPTER))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, DomainMessage.ERROR_USER_ADAPTER))
                .bodyToMono(APISuccessResponse.class)
                .doOnSuccess(response ->
                        log.info(RestConstants.MESSAGE_RESPONSE_SUCCESSFUL_SERVICE,
                                kv(RestConstants.RESPONSE_SUCCESSFUL_KEY, response)))
                .doOnError(error ->
                        log.error(RestConstants.MESSAGE_RESPONSE_ERROR_SERVICE,
                                kv(RestConstants.RESPONSE_ERROR_KEY, error.getMessage())))
                .map(apiSuccessResponse->
                        UserMapper.MAPPER.userDataToUser(apiSuccessResponse.getData()))
                .onErrorMap(Predicate.not(UserException.class::isInstance),
                        exception -> new UserException(exception, DomainMessage.INTERNAL_ERROR))
        );
    }

    private Mono<Throwable> buildErrorResponse(ClientResponse response, DomainMessage domainMessage) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty(RestConstants.MESSAGE_ERROR_WITH_DETAILS)
                .flatMap(errorBody -> {
                    log.error(RestConstants.MESSAGE_RESPONSE_ERROR_SERVICE, errorBody);
                    return Mono.error(
                            response.statusCode().is5xxServerError() ?
                                    new TechnicalException(domainMessage):
                                    new BusinessException(domainMessage));
                });
    }
}
