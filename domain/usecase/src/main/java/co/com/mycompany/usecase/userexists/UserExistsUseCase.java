package co.com.mycompany.usecase.userexists;

import java.util.UUID;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class UserExistsUseCase {

    private final UserRepositoryGateway userRepository;

    public Mono<Boolean> userExists(UUID id) {
        if (id == null || id.equals(new UUID(0L, 0L))) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_ARGUMENT, "Id cannot be null or empty"));
        }
        return userRepository.findById(id)
                .hasElement()       // Verify if exists at least one element and return Mono<Boolean>
                .onErrorMap(ex -> new BusinessException(ErrorCode.DB_ERROR, "Database error: " + ex.getMessage(), ex));
    }
}
