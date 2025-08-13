package co.com.nequi.r2dbc.repository;

import co.com.nequi.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long>,
        ReactiveQueryByExampleExecutor<UserEntity> {

    @Query("""
            SELECT u.*
            FROM users u
            WHERE LOWER(TRIM(u.name)) LIKE LOWER(CONCAT('%',TRIM(:name),'%'))
            """)
    Flux<UserEntity> findUsersByName(String name);

}
