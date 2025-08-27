package co.com.mycompany.r2dbc;

import co.com.mycompany.model.user.User;
import co.com.mycompany.model.user.gateways.UserRepositoryGetway;
import co.com.mycompany.r2dbc.entity.UserEntity;
import co.com.mycompany.r2dbc.helper.ReactiveAdapterOperations;
import reactor.core.publisher.Mono;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryAdapter extends ReactiveAdapterOperations<User, UserEntity, String, UserRepository> implements UserRepositoryGetway {
    
    public UserRepositoryAdapter(UserRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, User.Builder.class).build());
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email).map(this::toEntity);
    }

    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }


}
