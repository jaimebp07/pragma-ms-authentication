package co.com.mycompany.r2dbc;

import java.util.UUID;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import co.com.mycompany.r2dbc.entity.UserEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, UUID>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByEmail(String email);
    Flux<UserEntity> findAllByIdIn(Iterable<UUID> ids);
}
