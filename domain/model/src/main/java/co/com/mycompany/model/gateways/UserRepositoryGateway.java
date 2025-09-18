package co.com.mycompany.model.gateways;

import java.util.Set;
import java.util.UUID;

import co.com.mycompany.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryGateway {

    Flux<User> findAll();
    Mono<User> findByEmail(String email);
    Mono<User> save(User user);
    Mono<User> findById(UUID id);
    Mono<String> login(String email, String password);
    Mono<Set<User>> findByIdList(Set<UUID> idList);

}
