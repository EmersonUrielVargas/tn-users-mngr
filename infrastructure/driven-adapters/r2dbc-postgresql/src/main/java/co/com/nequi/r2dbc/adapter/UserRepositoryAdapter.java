package co.com.nequi.r2dbc.adapter;

import co.com.nequi.model.user.User;
import co.com.nequi.model.user.gateways.UserPersistenceGateway;
import co.com.nequi.r2dbc.entity.UserEntity;
import co.com.nequi.r2dbc.helper.ReactiveAdapterOperations;
import co.com.nequi.r2dbc.repository.UserRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<
    User,
    UserEntity,
    Long,
        UserRepository
> implements UserPersistenceGateway {
    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> upsertUser(User user) {
        user.setId(null);
        return this.save(user);
    }

    @Override
    public Mono<User> findUserById(Long id) {
        return this.findById(id);
    }
}
