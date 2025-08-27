package co.com.mycompany.usecase.registeruser;

import co.com.mycompany.model.user.User;
import co.com.mycompany.model.user.gateways.UserRepositoryGetway;
import reactor.core.publisher.Mono;

public class  RegisterUserUseCase {
    private final UserRepositoryGetway userRepository;

    public RegisterUserUseCase(UserRepositoryGetway userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> registerUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("Email already registered")))
                .switchIfEmpty(userRepository.save(user));
    }
}
