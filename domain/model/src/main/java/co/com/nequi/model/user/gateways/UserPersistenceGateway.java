package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserPersistenceGateway {
    Mono<User> insertUser(User user);
    Mono<User> findUserById(Long id);
    Flux<User> findAllUser();
    Flux<User> findAllUsersByName(String name);
}
