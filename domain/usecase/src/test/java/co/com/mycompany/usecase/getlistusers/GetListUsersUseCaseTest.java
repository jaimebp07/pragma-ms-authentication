package co.com.mycompany.usecase.getlistusers;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetListUsersUseCaseTest {
private UserRepositoryGateway userRepositoryGateway;
    private GetListUsersUseCase useCase;

    @BeforeEach
    void setUp() {
        userRepositoryGateway = Mockito.mock(UserRepositoryGateway.class);
        useCase = new GetListUsersUseCase(userRepositoryGateway);
    }

    private User buildSampleUser() {
        return new User.Builder()
                .id(UUID.randomUUID())
                .firstName("Alice")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Street")
                .phoneNumber("123456")
                .email("alice@example.com")
                .baseSalary(BigDecimal.valueOf(1000))
                .roles(List.of())  // sin roles para simplificar
                .password("secret")
                .build();
    }

    @Test
    void returnsUsersWhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        Set<User> users = Set.of(buildSampleUser());
        when(userRepositoryGateway.findByIdList(anySet()))
                .thenReturn(Mono.just(users));

        // Act & Assert
        StepVerifier.create(useCase.getListUsers(Set.of(id)))
                .expectNext(users)
                .verifyComplete();

        verify(userRepositoryGateway).findByIdList(Set.of(id));
    }

    @Test
    void throwsErrorWhenIdListIsNull() {
        StepVerifier.create(useCase.getListUsers(null))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.INVALID_ARGUMENT;
                })
                .verify();
        verifyNoInteractions(userRepositoryGateway);
    }

    @Test
    void throwsErrorWhenIdListIsEmpty() {
        StepVerifier.create(useCase.getListUsers(Set.of()))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.INVALID_ARGUMENT;
                })
                .verify();
        verifyNoInteractions(userRepositoryGateway);
    }

    @Test
    void throwsErrorWhenRepositoryReturnsEmptySet() {
        UUID id = UUID.randomUUID();
        when(userRepositoryGateway.findByIdList(anySet()))
                .thenReturn(Mono.just(Set.of())); // vacío

        StepVerifier.create(useCase.getListUsers(Set.of(id)))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BusinessException;
                    BusinessException ex = (BusinessException) error;
                    assert ex.getCode() == ErrorCode.USER_NOT_FOUND;
                })
                .verify();
    }

  
}
