package co.com.mycompany.usecase.userexists;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UserExistsUseCaseTest {

    private UserRepositoryGateway userRepository;
    private UserExistsUseCase userExistsUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepositoryGateway.class);
        userExistsUseCase = new UserExistsUseCase(userRepository);
    }

    @Test
    void shouldReturnTrueWhenUserExists() {
        UUID id = UUID.randomUUID();
        User user = new User.Builder()
                .id(id)
                .firstName("Pepito")
                .lastName("Perez")
                .birthDate(LocalDate.now())
                .email("test@test.com")
                .baseSalary(BigDecimal.valueOf(2000))
                .build();

        when(userRepository.findById(id)).thenReturn(Mono.just(user));

        StepVerifier.create(userExistsUseCase.userExists(id))
                .expectNext(true)
                .verifyComplete();

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(userExistsUseCase.userExists(id))
                .expectNext(false)
                .verifyComplete();

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowBusinessExceptionWhenIdIsNull() {
        StepVerifier.create(userExistsUseCase.userExists(null))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.INVALID_ARGUMENT;
                    assert ex.getMessage().contains("Id cannot be null");
                })
                .verify();
    }

    @Test
    void shouldThrowBusinessExceptionWhenIdIsEmptyUUID() {
        StepVerifier.create(userExistsUseCase.userExists(new UUID(0L, 0L)))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.INVALID_ARGUMENT;
                })
                .verify();
    }

    @Test
    void shouldMapDatabaseErrorToBusinessException() {
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Mono.error(new RuntimeException("DB down")));

        StepVerifier.create(userExistsUseCase.userExists(id))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.DB_ERROR;
                    assert ex.getMessage().contains("Database error");
                })
                .verify();

        verify(userRepository, times(1)).findById(id);
    }
}
