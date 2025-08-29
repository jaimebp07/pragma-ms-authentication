package co.com.mycompany.r2dbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import co.com.mycompany.model.user.User;
import co.com.mycompany.r2dbc.entity.UserEntity;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserMapperRepositoryAdapterTest {
    @Mock
    UserReactiveRepository userReactiveRepository;

    @Mock
    ObjectMapper mapper;

    @InjectMocks
    UserRepositoryAdapter repositoryAdapter;

    @Test
    void testFindByEmailReturnsUser() {
        // Arrange
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
            .build();

        when(userReactiveRepository.findByEmail(email)).thenReturn(Mono.just(entity));
        when(mapper.mapBuilder(entity, User.Builder.class)).thenReturn(user.toBuilder());

        StepVerifier.create(repositoryAdapter.findByEmail(email))
                .expectNextMatches(u -> u.getEmail().equals(email))
                .verifyComplete();
    }

    
    @Test
    void testSaveReturnsUser() {
        User user = new User.Builder()
            .id(UUID.randomUUID())
            .firstName("Andres")
            .lastName("Lopez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123")
            .phoneNumber("3001234567")
            .email("save@test.com")
            .baseSalary(BigDecimal.valueOf(3000))
            .build();

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());

        when(mapper.map(user, UserEntity.class)).thenReturn(entity); // de dominio a entidad
        when(userReactiveRepository.save(entity)).thenReturn(Mono.just(entity)); // repo guarda
        when(mapper.mapBuilder(entity, User.Builder.class)).thenReturn(user.toBuilder()); // de entidad a dominio

        StepVerifier.create(repositoryAdapter.save(user))
                .expectNextMatches(u -> u.getEmail().equals("save@test.com"))
                .verifyComplete();
    }

}
