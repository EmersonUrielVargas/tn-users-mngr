package co.com.nequi.model.user.gateways;

import co.com.nequi.model.user.User;
import reactor.core.publisher.Mono;

public interface UserNotificationGateway {
    Mono<String> notifyNewUser(User user);
}
