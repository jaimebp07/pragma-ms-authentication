package co.com.mycompany.model.user.gateways;

import co.com.mycompany.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryGetway {

    Flux<User> findAll();
    Mono<User> findByEmail(String email);
    Mono<User> save(User user);
}
