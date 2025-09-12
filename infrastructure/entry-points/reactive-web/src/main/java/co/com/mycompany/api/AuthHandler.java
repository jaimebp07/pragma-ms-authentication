package co.com.mycompany.api;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.mycompany.api.dto.LoginRqDTO;
import co.com.mycompany.api.dto.LoginRsDTO;
import co.com.mycompany.model.exceptions.BusinessException;
import co.com.mycompany.usecase.login.LogInUseCase;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Component;

@Component
public class AuthHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthHandler.class);
    private final LogInUseCase loginUseCase;

    public AuthHandler(LogInUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public Mono<ServerResponse> handleLogin(ServerRequest request) {
        return request.bodyToMono(LoginRqDTO.class)
                .doOnNext(body -> log.info("Login request for email={}", body.email()))
                .flatMap(body -> loginUseCase.login(body.email(), body.password()))
                .flatMap(token -> {
                    LoginRsDTO resp = new LoginRsDTO(token, "Bearer", "TODO_EXPIRES"); // opcional calcular expiration
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(resp);
                })
                .onErrorResume(BusinessException.class, ex -> {
                    log.warn("business error: {}", ex.getMessage());
                    HttpStatus status = switch (ex.getCode()) {
                        case USER_NOT_FOUND, INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
                        default -> HttpStatus.BAD_REQUEST;
                    };

                    return ServerResponse.status(status)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse(ex.getCode().name(), ex.getMessage()));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("unexpected error: ", ex);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new ErrorResponse("INTERNAL_ERROR", "Unexpected error occurred"));
                });
    }

     record ErrorResponse(String code, String message) {}
}
