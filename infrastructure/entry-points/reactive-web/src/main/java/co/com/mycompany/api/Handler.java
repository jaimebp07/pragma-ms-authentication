package co.com.mycompany.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.mycompany.api.mapper.UserMapper;
import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.api.dto.UserDTO;
import co.com.mycompany.usecase.registeruser.RegisterUserUseCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private  final RegisterUserUseCase registerUserUseCase;
    private final UserMapper userMapper;

    public Mono<ServerResponse> handleRegisterUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserDTO.class)
                .doOnNext(dto -> log.info("📥 Request received: {}", dto))
                .map(userMapper::toDomain)
                .flatMap(registerUserUseCase::registerUser)
                .map(userMapper::toDTO)
                .flatMap(userDTO -> ServerResponse
                        .created(serverRequest.uri())
                        .bodyValue(userDTO)
                )
                .doOnSuccess(resp -> log.info("✅ Response created successfully"))
                .doOnError(ex -> log.error("❌ Error in the handler", ex))
                .onErrorResume(BusinessException.class, ex -> {
                        log.warn("⚠️ business mistake: {}", ex.getMessage());
                        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse("BUSINESS_ERROR", ex.getMessage()));
                        }
                )
                .onErrorResume(Exception.class, ex -> {
                        log.error("💥 Unexpected error: ", ex);
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse("INTERNAL_ERROR", "Unexpected error occurred"));
                        }
                );
    }

    private record ErrorResponse(String code, String message) {}
}
