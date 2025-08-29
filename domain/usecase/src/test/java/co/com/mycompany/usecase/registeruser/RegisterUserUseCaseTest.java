package co.com.mycompany.usecase.registeruser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RegisterUserUseCaseTest {

    private UserRepositoryGateway userRepository;
    private RegisterUserUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryGateway.class);
        useCase = new RegisterUserUseCase(userRepository);
    }

    private User buildUser(String email) {
        return new User.Builder()
                .id(UUID.randomUUID())
                .firstName("Andres")
                .lastName("Lopez")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Calle 123")
                .phoneNumber("3001234567")
                .email(email)
                .baseSalary(BigDecimal.valueOf(3000))
                .build();
    }

    @Test
    void shouldRegisterUserWhenValidAndNotExists() {
        User user = buildUser("andres@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(useCase.registerUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository).findByEmail("andres@test.com");
        verify(userRepository).save(user);
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        User existingUser = buildUser("andres@test.com");

        when(userRepository.findByEmail("andres@test.com")).thenReturn(Mono.just(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.registerUser(existingUser))
                .expectErrorMatches(error -> 
                    
                    error instanceof BusinessException &&
                    error.getMessage().equals("Email already registered")
                )
                .verify();
    }

}
