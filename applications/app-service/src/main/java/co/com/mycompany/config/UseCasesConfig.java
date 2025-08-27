package co.com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.mycompany.model.user.gateways.UserRepositoryGetway;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;

@Configuration
public class UseCasesConfig {

        @Bean
        public RegisterUserUseCase registerUserUseCase(UserRepositoryGetway userRepository) {
                return new RegisterUserUseCase(userRepository);
        }
}
