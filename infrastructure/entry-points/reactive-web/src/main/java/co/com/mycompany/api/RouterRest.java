package co.com.mycompany.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.mycompany.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/usuarios",
            produces = { "application/json" },
            beanClass = Handler.class,
            beanMethod = "registerUseCase",
            operation = @Operation(
                operationId = "createUser",
                summary = "Registrar usuario",
                description = "Registra un nuevo usuario en el sistema",
                tags = { "Usuarios" },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del usuario a registrar",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class)
                    )
                ),
                responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario creado",
                                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                                    @ApiResponse(responseCode = "400", description = "Error de validación",
                                        content = @Content(schema = @Schema(implementation = String.class))),
                                    @ApiResponse(responseCode = "500", description = "Error interno",
                                        content = @Content(schema = @Schema(implementation = String.class)))
                            }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/usuarios"), handler::handleRegisterUser);
    }
}
