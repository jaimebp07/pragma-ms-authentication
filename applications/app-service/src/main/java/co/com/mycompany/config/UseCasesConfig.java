package co.com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.mycompany.model.gateways.TransactionalGateway;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;

@Configuration
public class UseCasesConfig {

        @Bean
        RegisterUserUseCase registerUserUseCase(UserRepositoryGateway userRepository,
                                                   TransactionalGateway transactionalGateway) {
                return new RegisterUserUseCase(userRepository, transactionalGateway);
        }
}
