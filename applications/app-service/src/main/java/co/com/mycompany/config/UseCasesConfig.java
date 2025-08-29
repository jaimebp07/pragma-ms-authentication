package co.com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;

@Configuration
public class UseCasesConfig {

        @Bean
        RegisterUserUseCase registerUserUseCase(UserRepositoryGateway userRepository) {
                return new RegisterUserUseCase(userRepository);
        }
}
