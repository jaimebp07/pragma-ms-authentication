package co.com.mycompany.r2dbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import co.com.mycompany.model.authentication.Role;
import co.com.mycompany.model.gateways.PasswordServiceGateway;
import co.com.mycompany.model.gateways.TokenServiceGateway;
import co.com.mycompany.model.user.User;
import co.com.mycompany.r2dbc.entity.UserEntity;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class UserMapperRepositoryAdapterTest {
    @Mock
    UserReactiveRepository userReactiveRepository;

    @Mock
    ObjectMapper mapper;

    @Mock 
    PasswordServiceGateway passwordServicePort;   // <-- NUEVO
    
    @Mock
    TokenServiceGateway tokenService; 

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Test
    void testFindByEmailReturnsUser() {
        
        List<Role> roles = List.of(Role.ADMIN);
        String email = "test@example.com";

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setEmail(email);

        User user = new User.Builder()
            .id(UUID.randomUUID())
            .firstName("Andres")
            .lastName("Lopez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phoneNumber("3001234567")
            .email(email)
            .baseSalary(BigDecimal.valueOf(3000))
            .roles(roles)
            .password("12345678")
            .build();

        when(userReactiveRepository.findByEmail(email)).thenReturn(Mono.just(entity));
        when(mapper.mapBuilder(entity, User.Builder.class)).thenReturn(user.toBuilder());

        StepVerifier.create(repositoryAdapter.findByEmail(email))
                .expectNextMatches(u -> u.getEmail().equals(email))
                .verifyComplete();
    }

    
    @Test
    void testSaveReturnsUser() {
        List<Role> roles = List.of(Role.ADMIN);

        User user = new User.Builder()
            .id(UUID.randomUUID())
            .firstName("Andres")
            .lastName("Lopez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phoneNumber("3001234567")
            .email("save@test.com")
            .baseSalary(BigDecimal.valueOf(3000))
            .roles(roles)
            .password("12345678")
            .build();

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());

        when(passwordServicePort.encode(user.getPassword()))
                .thenReturn("encoded-pass");
        // usa any() para no depender de la igualdad exacta
        when(mapper.map(any(User.class), eq(UserEntity.class)))
                .thenReturn(entity);
        when(userReactiveRepository.save(entity))
                .thenReturn(Mono.just(entity));
        when(mapper.mapBuilder(any(UserEntity.class), eq(User.Builder.class)))
                .thenReturn(user.toBuilder());

        StepVerifier.create(repositoryAdapter.save(user))
                .expectNextMatches(u -> u.getEmail().equals("save@test.com"))
                .verifyComplete();
        /*when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(userReactiveRepository.save(entity)).thenReturn(Mono.just(entity)); 
        when(mapper.mapBuilder(entity, User.Builder.class)).thenReturn(user.toBuilder()); 

        StepVerifier.create(repositoryAdapter.save(user))
                .expectNextMatches(u -> u.getEmail().equals("save@test.com"))
                .verifyComplete();*/
    }

}
