package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Mono;

public interface UserCacheGateway {
    Mono<User> saveUser(User user);

    Mono<User> getUser(Long id);

}
