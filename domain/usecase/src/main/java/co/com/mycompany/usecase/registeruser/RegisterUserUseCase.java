package co.com.mycompany.usecase.registeruser;

import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.model.exceptions.ErrorCode;
import co.com.mycompany.model.gateways.TransactionalGateway;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.model.user.User;
import co.com.mycompany.model.user.validator.UserValidator;
import reactor.core.publisher.Mono;

public class  RegisterUserUseCase {

        private final UserRepositoryGateway userRepository;
        private final TransactionalGateway transactionalGateway;

        public RegisterUserUseCase(UserRepositoryGateway userRepository,
                                TransactionalGateway transactionalGateway) {
                this.userRepository = userRepository;
                this.transactionalGateway = transactionalGateway;
        }

        public Mono<User> registerUser(User user) {
                return transactionalGateway.execute(
                        Mono.fromCallable(() -> {
                                System.out.println("--------------> user roles "+ user.getRoles() + " password " + user.getPassword());
                                UserValidator.validate(user);
                                return user;
                        })
                        .flatMap(validUser -> userRepository.findByEmail(validUser.getEmail())
                                .flatMap(existing -> Mono.<User>error(
                                        new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already registered")))
                                .switchIfEmpty(userRepository.save(validUser))
                        )
                        .onErrorMap(IllegalArgumentException.class,
                                e -> new BusinessException(ErrorCode.VALIDATION_ERROR, "Validation error: " + e.getMessage(), e)
                        )
                );
        }
}
