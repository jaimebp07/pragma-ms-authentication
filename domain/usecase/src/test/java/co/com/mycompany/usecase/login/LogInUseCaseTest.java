package co.com.mycompany.usecase.login;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import co.com.mycompany.model.gateways.UserRepositoryGateway;

@ExtendWith(MockitoExtension.class)
class LogInUseCaseTest {

    @Mock
    private UserRepositoryGateway userRepository;

    private LogInUseCase logInUseCase;

    @BeforeEach
    void setUp() {
        logInUseCase = new LogInUseCase(userRepository);
    }

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        String email = "test@example.com";
        String password = "123456";
        String expectedToken = "jwt-token";

        // Mockeamos el gateway
        when(userRepository.login(email, password)).thenReturn(Mono.just(expectedToken));

        StepVerifier.create(logInUseCase.login(email, password))
                .expectNextMatches(token -> token.equals(expectedToken))
                .verifyComplete();
    }

    @Test
    void loginShouldReturnEmptyMonoWhenUserNotFound() {
        String email = "notfound@example.com";
        String password = "123456";

        // Mockeamos que el usuario no existe (retorna Mono.empty())
        when(userRepository.login(email, password)).thenReturn(Mono.empty());

        StepVerifier.create(logInUseCase.login(email, password))
                .verifyComplete();  // No emite ningún valor
    }

    @Test
    void loginShouldPropagateError() {
        String email = "error@example.com";
        String password = "123456";
        RuntimeException exception = new RuntimeException("Something went wrong");

        when(userRepository.login(email, password)).thenReturn(Mono.error(exception));

        StepVerifier.create(logInUseCase.login(email, password))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Something went wrong"))
                .verify();
    }
}

