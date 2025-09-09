package co.com.mycompany.api;

import co.com.mycompany.api.dto.UserDTO;
import co.com.mycompany.model.user.User;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;
import co.com.mycompany.usecase.userexists.UserExistsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import co.com.mycompany.api.mapper.UserMapper;

import java.net.URI;
import java.util.UUID;
import co.com.mycompany.api.Handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HandlerTest {

    private RegisterUserUseCase registerUserUseCase;
    private UserExistsUseCase userExistsUseCase;
    private UserMapper userMapper;
    private Handler handler;
    private ServerRequest serverRequest;

    @BeforeEach
    void setUp() {
        registerUserUseCase = mock(RegisterUserUseCase.class);
        userExistsUseCase = mock(UserExistsUseCase.class);
        userMapper = new UserMapper() {
            @Override
            public UserDTO toDTO(User domain) {
                return new UserDTO(domain.getFirstName(), domain.getLastName(), domain.getBirthDate(),
                        domain.getAddress(), domain.getPhoneNumber(), domain.getEmail(), domain.getBaseSalary());
            }
        };
        handler = new Handler(registerUserUseCase, userMapper, userExistsUseCase);

        serverRequest = mock(ServerRequest.class);
        when(serverRequest.uri()).thenReturn(URI.create("/api/v1/usuarios"));
    }

    @Test
    void testHandleRegisterUser_Success() {
        UserDTO userDTO = new UserDTO("Juan", "Perez", null, "Address", "123456", "juan@example.com", null);
        User user = new User.Builder()
                .firstName("Juan").lastName("Perez").email("juan@example.com").build();

        when(serverRequest.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));
        when(registerUserUseCase.registerUser(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(handler.handleRegisterUser(serverRequest))
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(registerUserUseCase, times(1)).registerUser(any(User.class));
    }

    @Test
    void testHandleRegisterUser_BusinessException() {
        UserDTO userDTO = new UserDTO("Juan", "Perez", null, "Address", "123456", "juan@example.com", null);
        when(serverRequest.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));
        when(registerUserUseCase.registerUser(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("Business error")));

        StepVerifier.create(handler.handleRegisterUser(serverRequest))
                .expectNextMatches(resp -> resp.statusCode().is5xxServerError())
                .verifyComplete();
    }

    @Test
    void testHandleGetUserById_UserExists() {
        UUID userId = UUID.randomUUID();
        when(serverRequest.pathVariable("id")).thenReturn(userId.toString());
        when(userExistsUseCase.userExists(userId)).thenReturn(Mono.just(true));

        StepVerifier.create(handler.handleGetUserById(serverRequest))
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testHandleGetUserById_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(serverRequest.pathVariable("id")).thenReturn(userId.toString());
        when(userExistsUseCase.userExists(userId)).thenReturn(Mono.just(false));

        StepVerifier.create(handler.handleGetUserById(serverRequest))
                .expectNextMatches(resp -> resp.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void testHandleGetUserById_BusinessException() {
        UUID userId = UUID.randomUUID();
        when(serverRequest.pathVariable("id")).thenReturn(userId.toString());
        when(userExistsUseCase.userExists(userId))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(handler.handleGetUserById(serverRequest))
                .expectNextMatches(resp -> resp.statusCode().is5xxServerError())
                .verifyComplete();
    }
}
