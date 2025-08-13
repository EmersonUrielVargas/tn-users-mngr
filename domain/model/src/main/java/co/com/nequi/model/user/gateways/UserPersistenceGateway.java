package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Mono;

public interface UserPersistenceGateway {
    Mono<User> upsertUser(User user);
    Mono<User> findUserById(Long id);
}
