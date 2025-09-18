package co.com.mycompany.api;

import co.com.mycompany.api.dto.CustomerRsDTO;
import co.com.mycompany.api.dto.UserDTO;
import co.com.mycompany.model.authentication.Role;
import co.com.mycompany.model.user.User;
import co.com.mycompany.usecase.getlistusers.GetListUsersUseCase;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;
import co.com.mycompany.usecase.userexists.UserExistsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import co.com.mycompany.api.mapper.CustomerMapper;
import co.com.mycompany.api.mapper.UserMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HandlerTest {

    private RegisterUserUseCase registerUserUseCase;
    private UserExistsUseCase userExistsUseCase;
    private UserMapper userMapper;
    private Handler handler;
    private ServerRequest serverRequest;
    private GetListUsersUseCase getListUsersUseCase;
    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        registerUserUseCase = mock(RegisterUserUseCase.class);
        userExistsUseCase = mock(UserExistsUseCase.class);
        getListUsersUseCase = mock(GetListUsersUseCase.class);

        userMapper = new UserMapper() {
            @Override
            public UserDTO toDTO(User domain) {
                List<String> roleNames = domain.getRoles().stream()
                                       .map(Role::name)
                                       .toList();
                return new UserDTO(
                    domain.getFirstName(),
                    domain.getLastName(),
                    domain.getBirthDate(),
                    domain.getAddress(),
                    domain.getPhoneNumber(),
                    domain.getEmail(),
                    domain.getBaseSalary(),
                    roleNames,
                    domain.getPassword()
                );
            }
        };

        customerMapper = new CustomerMapper() {

            @Override
            public CustomerRsDTO toCustomerDto(User user) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'toCustomerDto'");
            }

            @Override
            public Set<CustomerRsDTO> toCustomerDtoSet(Set<User> users) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'toCustomerDtoSet'");
            }
            
        };
        handler = new Handler(registerUserUseCase, userMapper, userExistsUseCase, getListUsersUseCase, customerMapper);

        serverRequest = mock(ServerRequest.class);
        when(serverRequest.uri()).thenReturn(URI.create("/api/v1/usuarios"));
    }

    @Test
    void testHandleRegisterUser_Success() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        UserDTO userDTO = new UserDTO("Juan", "Perez", null, "Address", "123456", "juan@example.com", null, roles, "12345678");
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
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        UserDTO userDTO = new UserDTO("Juan", "Perez", null, "Address", "123456", "juan@example.com", null, roles, "12345678");
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
