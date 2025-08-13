package co.com.nequi.api.handler;


import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.api.util.GenerateResponse;
import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.DomainException;
import co.com.nequi.model.exceptions.EntityNotFoundException;
import co.com.nequi.model.exceptions.InvalidValueParamException;
import co.com.nequi.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserHandler {
    private final UserUseCase userUseCase;

    public Mono<ServerResponse> postCreateUser(ServerRequest serverRequest) {
        Long newUserId = Long.valueOf(serverRequest.pathVariable("id"));
        return userUseCase.createUser(newUserId)
                .flatMap(userCreated ->
                        ServerResponse.status(201)
                        .bodyValue(userCreated))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainMessage())
                ).onErrorResume(exception -> {
                    log.error(CommonConstants.MESSAGE_INTERNAL_SERVER_ERROR, kv("exception", exception.getMessage()));
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainMessage.INTERNAL_ERROR);
                });
    }
}
