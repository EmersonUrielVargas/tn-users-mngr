package co.com.nequi.usecase.user;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserCacheGateway;
import co.com.nequi.model.user.gateways.UserExternalSourceGateway;
import co.com.nequi.model.user.gateways.UserNotificationGateway;
import co.com.nequi.model.user.gateways.UserPersistenceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserPersistenceGateway userPersistenceGateway;
    private final UserExternalSourceGateway userExternalSourceGateway;
    private final UserCacheGateway userCacheGateway;
    private final UserNotificationGateway userNotificationGateway;

    public Mono<User> createUser(Long userId){
        return userPersistenceGateway.findUserById(userId)
                .switchIfEmpty(
                    userExternalSourceGateway.findUser(userId)
                    .flatMap(userToCreate ->
                        Mono.defer(()->
                            userPersistenceGateway.insertUser(userToCreate)
                            .flatMap(userCreate ->
                                userNotificationGateway.notifyNewUser(userCreate)
                                .thenReturn(userCreate)
                            )
                            .switchIfEmpty(Mono.error(new BusinessException(DomainMessage.USER_CREATION_FAIL))))
                    )
                );
    }

    public Mono<User> findUserById(Long userId){
        return userCacheGateway.getUser(userId)
                .switchIfEmpty(
                    userPersistenceGateway.findUserById(userId)
                    .flatMap(userFound ->
                        userCacheGateway.saveUser(userFound).onErrorReturn(userFound)
                    ).switchIfEmpty(
                        Mono.error(new BusinessException(DomainMessage.USER_NOT_FOUND))
                    )
                );
    }

    public Flux<User> findAllUser(){
        return userPersistenceGateway.findAllUser();
    }

    public Flux<User> findAllUsersByName(String name){
        return userPersistenceGateway.findAllUsersByName(name);
    }


}
