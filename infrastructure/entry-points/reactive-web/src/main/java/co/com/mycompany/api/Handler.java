package co.com.mycompany.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.mycompany.api.mapper.UserMapper;
import co.com.mycompany.api.dto.UserDTO;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;

@Component
@RequiredArgsConstructor
public class Handler {

    private  final RegisterUserUseCase registerUseCase;

    private final UserMapper userMapper;

    public Mono<ServerResponse> registerUseCase(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(UserDTO.class)
                .map(userMapper::toDomain)
                .flatMap(registerUseCase::registerUser)
                .map(userMapper::toDTO)
                .flatMap(userDTO -> ServerResponse
                        .created(serverRequest.uri())
                        .bodyValue(userDTO)
                )
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }

}
