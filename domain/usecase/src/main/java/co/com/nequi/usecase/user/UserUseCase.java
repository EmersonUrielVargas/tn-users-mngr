package co.com.nequi.usecase.user;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.DomainException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserExternalSourceGateway;
import co.com.nequi.model.user.gateways.UserPersistenceGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserPersistenceGateway userPersistenceGateway;
    private final UserExternalSourceGateway userExternalSourceGateway;

    public Mono<User> createUser(Long userId){
        return userPersistenceGateway.findUserById(userId)
                .switchIfEmpty(
                    userExternalSourceGateway.findUser(userId)
                    .flatMap(userToCreate ->
                        Mono.defer(()->
                            userPersistenceGateway.upsertUser(userToCreate)
                            .switchIfEmpty(Mono.error(new DomainException(DomainMessage.USER_CREATION_FAIL))))
                    )
                );
    }


}
