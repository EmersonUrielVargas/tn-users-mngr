package co.com.nequi.api.handler;


import co.com.nequi.api.constants.CommonConstants;
import co.com.nequi.api.mapper.UserDetailsMapper;
import co.com.nequi.api.util.GenerateResponse;
import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.exceptions.TechnicalException;
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
        Long newUserId = Long.valueOf(serverRequest.pathVariable(CommonConstants.PATH_PARAM_ID));
        return userUseCase.createUser(newUserId)
                .map(UserDetailsMapper.MAPPER::userToUserDetails)
                .flatMap(userCreated ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(userCreated))
                .onErrorResume(TechnicalException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDomainMessage()))
                .onErrorResume(BusinessException.class, ex ->
                        GenerateResponse.generateErrorResponse(GenerateResponse.getStatusResponse(ex.getDomainMessage()), ex.getDomainMessage())
                ).onErrorResume(exception -> {
                    log.error(CommonConstants.MESSAGE_INTERNAL_SERVER_ERROR, kv(CommonConstants.LOG_EXCEPTION_KEY, exception.getMessage()));
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        Long newUserId = Long.valueOf(serverRequest.pathVariable(CommonConstants.PATH_PARAM_ID));
        return userUseCase.findUserById(newUserId)
                .map(UserDetailsMapper.MAPPER::userToUserDetails)
                .flatMap(userFound ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(userFound))
                .onErrorResume(TechnicalException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDomainMessage()))
                .onErrorResume(BusinessException.class, ex ->
                        GenerateResponse.generateErrorResponse(GenerateResponse.getStatusResponse(ex.getDomainMessage()), ex.getDomainMessage())
                ).onErrorResume(exception -> {
                    log.error(CommonConstants.MESSAGE_INTERNAL_SERVER_ERROR, kv(CommonConstants.LOG_EXCEPTION_KEY, exception.getMessage()));
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        return serverRequest.queryParam(CommonConstants.QUERY_PARAM_FILTER_NAME)
                .map(userUseCase::findAllUsersByName)
                .orElse(userUseCase.findAllUser())
                .map(UserDetailsMapper.MAPPER::userToUserDetails)
                .collectList()
                .flatMap(usersList ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(usersList))
                .onErrorResume(TechnicalException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDomainMessage()))
                .onErrorResume(BusinessException.class, ex ->
                        GenerateResponse.generateErrorResponse(GenerateResponse.getStatusResponse(ex.getDomainMessage()), ex.getDomainMessage())
                ).onErrorResume(exception -> {
                    log.error(CommonConstants.MESSAGE_INTERNAL_SERVER_ERROR, kv(CommonConstants.LOG_EXCEPTION_KEY, exception.getMessage()));
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainMessage.INTERNAL_ERROR);
                });
    }
}
