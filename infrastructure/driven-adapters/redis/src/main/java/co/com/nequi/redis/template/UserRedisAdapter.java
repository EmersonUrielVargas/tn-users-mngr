package co.com.nequi.redis.template;

import co.com.nequi.model.enums.DomainMessage;
import co.com.nequi.model.exceptions.BusinessException;
import co.com.nequi.model.exceptions.TechnicalException;
import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserCacheGateway;
import co.com.nequi.redis.template.helper.ReactiveTemplateAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Primary
@Component
public class UserRedisAdapter extends ReactiveTemplateAdapterOperations<User, String, User>
implements UserCacheGateway
{
    public UserRedisAdapter(ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return super.save(generateKey(user.getId()), user, 60000)
                .doOnSuccess(success -> log.info("Redis Save Response", kv("saveRedisRS", success)))
                .doOnError(error -> log.info("Redis Save Error Response", kv("saveRedisErrorRS", error)))
                .onErrorMap(Predicate.not(BusinessException.class::isInstance), exception -> new TechnicalException(DomainMessage.CREATE_USER_CACHE_INTERNAL_ERROR));
    }

    @Override
    public Mono<User> getUser(Long id) {
        return this.findById(generateKey(id))
                .doOnSuccess(success -> log.info("Redis Response", kv("getRedisRS", success)))
                .doOnError(error -> log.info("Redis Error Response", kv("getRedisErrorRS", error)))
                .onErrorMap(Predicate.not(BusinessException.class::isInstance), exception -> new TechnicalException(DomainMessage.USER_GET_CACHE_INTERNAL_ERROR));
    }

    private String generateKey(Long id) {
        return "user:" + id;
    }
}
