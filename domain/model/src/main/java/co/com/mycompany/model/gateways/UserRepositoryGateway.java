package co.com.mycompany.model.gateways;

import co.com.mycompany.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryGateway {

    Flux<User> findAll();
    Mono<User> findByEmail(String email);
    Mono<User> save(User user);
}
