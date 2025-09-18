package co.com.mycompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.mycompany.model.gateways.TransactionalGateway;
import co.com.mycompany.model.gateways.UserRepositoryGateway;
import co.com.mycompany.usecase.getlistusers.GetListUsersUseCase;
import co.com.mycompany.usecase.login.LogInUseCase;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;
import co.com.mycompany.usecase.userexists.UserExistsUseCase;

@Configuration
public class UseCasesConfig {

        @Bean
        RegisterUserUseCase registerUserUseCase(UserRepositoryGateway userRepository,
                                                   TransactionalGateway transactionalGateway) {
                return new RegisterUserUseCase(userRepository, transactionalGateway);
        }

        @Bean
        UserExistsUseCase userExistsUseCase(UserRepositoryGateway userRepository) {
                return new UserExistsUseCase(userRepository);
        }

        @Bean
        LogInUseCase logInUseCase(UserRepositoryGateway userRepository) {
                return new LogInUseCase(userRepository);
        }

        @Bean
        GetListUsersUseCase getListUsersUseCase(UserRepositoryGateway userRepository) {
                return new GetListUsersUseCase(userRepository);
        }
}
