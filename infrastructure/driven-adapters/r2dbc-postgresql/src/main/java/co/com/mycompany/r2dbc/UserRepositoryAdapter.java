package co.com.mycompany.r2dbc;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.PasswordServiceGateway;
import co.com.mycompany.model.gateways.TokenServiceGateway;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import co.com.mycompany.r2dbc.entity.UserEntity;
import co.com.mycompany.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import java.util.UUID;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, UUID, UserReactiveRepository> implements UserRepositoryGateway {
    
    private final PasswordServiceGateway passwordServicePort;
    private final TokenServiceGateway tokenService;

    public UserRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,  PasswordServiceGateway passwordServicePort, TokenServiceGateway tokenService) {
        super(repository, mapper, d -> mapper.mapBuilder(d, User.Builder.class).build());
        this.tokenService = tokenService;
        this.passwordServicePort = passwordServicePort;
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toEntity);
    }

    @Override
    public Mono<User> save(User user) {
        User userToSave = user.toBuilder()
        .password(passwordServicePort.encode(user.getPassword()))
        .build();
        return super.save(userToSave);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return super.findById(id);
    }

    @Override
    public Mono<String> login(String email, String password) {
        return repository.findByEmail(email)
        .flatMap(userEntity -> {
            if (passwordServicePort.matches(password, userEntity.getPassword())) {
                User user = mapper.mapBuilder(userEntity, User.Builder.class).build();
                return Mono.just(tokenService.generateToken(user));
            } else {
                return Mono.error(new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Invalid credentials"));
            }
        })
        .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found"))); 
    }
}
