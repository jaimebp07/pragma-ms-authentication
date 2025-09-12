package co.com.mycompany.usecase.login;

import co.com.mycompany.model.gateways.UserRepositoryGateway;
import reactor.core.publisher.Mono;

public class LogInUseCase {

    private final UserRepositoryGateway userRepository;

    public LogInUseCase(UserRepositoryGateway userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<String> login(String email, String password) {
        return userRepository.login(email, password);
    }
}
