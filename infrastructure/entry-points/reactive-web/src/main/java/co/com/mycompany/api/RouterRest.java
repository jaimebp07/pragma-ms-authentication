package co.com.mycompany.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.mycompany.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/usuarios",
            produces = { "application/json" },
            consumes = { "application/json" },
            beanClass = Handler.class,
            beanMethod = "handleRegisterUser",
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
        ),
        @RouterOperation(
            path = "/api/v1/usuarios/{id}",
            produces = { "application/json" },
            beanClass = Handler.class,
            beanMethod = "handleGetUserById",
            operation = @Operation(
                operationId = "getUserById",
                summary = "Consultar usuario por ID",
                description = "Obtiene un usuario existente a partir de su identificador único",
                tags = { "Usuarios" },
                parameters = {
                    @Parameter(
                        name = "id",
                        description = "Identificador único del usuario",
                        required = true,
                        in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                        schema = @Schema(type = "string", format = "uuid")
                    )
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                        content = @Content(schema = @Schema(example = "{\"exists\": true, \"message\": \"User exists\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                        content = @Content(schema = @Schema(example = "{\"exists\": false, \"message\": \"User not found\"}"))),
                    @ApiResponse(responseCode = "400", description = "Error de validación",
                        content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno",
                        content = @Content(schema = @Schema(implementation = String.class)))
                }
            )
        )
    })
    RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return RouterFunctions.nest(path("/api/v1/usuarios"),
            route(POST(""), handler::handleRegisterUser)
            .andRoute(GET("/{id}"), handler::handleGetUserById)
        );
    }
}
