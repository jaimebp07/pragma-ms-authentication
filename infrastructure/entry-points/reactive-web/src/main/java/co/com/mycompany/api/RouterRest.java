package co.com.mycompany.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.mycompany.api.dto.LoginRqDTO;
import co.com.mycompany.api.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
                description = "Registra un nuevo usuario en el sistema con roles y contraseña",
                tags = { "Usuarios" },
                security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth") },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del usuario a registrar, incluyendo roles y contraseña",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class),
                        examples = {
                            @ExampleObject(
                                name = "Ejemplo Usuario",
                                value = """
                                    {
                                        "firstName": "Andrés",
                                        "lastName": "Pérez",
                                        "birthDate": "1995-08-15",
                                        "address": "Calle 123 #45-67",
                                        "phoneNumber": "3001234567",
                                        "email": "andres.perez@example.com",
                                        "baseSalary": 2500000.50,
                                        "roles": ["ADMIN"],
                                        "password": "123456"
                                    }
                                """
                            )
                        }
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "201",
                        description = "Usuario creado",
                        content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Error de validación",
                        content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "No autorizado, token inválido o ausente",
                        content = @Content(schema = @Schema(example = "{\"code\":\"UNAUTHORIZED\", \"message\":\"Invalid or missing token\"}"))
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno",
                        content = @Content(schema = @Schema(implementation = String.class))
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/usuarios/{id}/exists",
            produces = { "application/json" },
            beanClass = Handler.class,
            beanMethod = "handleGetUserById",
            operation = @Operation(
                operationId = "checkUserExists",
                summary = "Verificar si un usuario existe",
                description = "Consulta si un usuario existe en el sistema a partir de su identificador único",
                tags = { "Usuarios" },
                security = { @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth") },
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
                    @ApiResponse(
                        responseCode = "200",
                        description = "Resultado de la verificación",
                        content = @Content(
                            schema = @Schema(example = "{\"exists\": true, \"message\": \"User exists\"}")
                        )
                    ),
                    @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content = @Content(
                            schema = @Schema(example = "{\"exists\": false, \"message\": \"User not found\"}")
                        )
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Error de validación",
                        content = @Content(schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "No autorizado, token inválido o ausente",
                        content = @Content(schema = @Schema(example = "{\"code\":\"UNAUTHORIZED\", \"message\":\"Invalid or missing token\"}"))
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno",
                        content = @Content(schema = @Schema(implementation = String.class))
                    )
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/login",
            produces = { "application/json" },
            consumes = { "application/json" },
            beanClass = AuthHandler.class,
            beanMethod = "handleLogin",
            operation = @Operation(
                operationId = "login",
                summary = "Iniciar sesión",
                description = "Autentica un usuario con su email y contraseña, devolviendo un token JWT",
                tags = { "Autenticación" },
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credenciales del usuario para autenticación",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LoginRqDTO.class),
                        examples = {
                            @io.swagger.v3.oas.annotations.media.ExampleObject(
                                name = "Ejemplo Login",
                                value = """
                                {
                                    "email": "andres.perez@example.com",
                                    "password": "123456"
                                }
                                """
                            )
                        }
                    )
                ),
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Login exitoso, token devuelto",
                        content = @Content(schema = @Schema(implementation = co.com.mycompany.api.dto.LoginRsDTO.class))
                    ),
                    @ApiResponse(
                        responseCode = "401",
                        description = "Credenciales inválidas",
                        content = @Content(schema = @Schema(example = "{\"code\":\"INVALID_CREDENTIALS\", \"message\":\"Invalid credentials\"}"))
                    ),
                    @ApiResponse(
                        responseCode = "400",
                        description = "Error de validación",
                        content = @Content(schema = @Schema(example = "{\"code\":\"VALIDATION_ERROR\", \"message\":\"Invalid email format\"}"))
                    ),
                    @ApiResponse(
                        responseCode = "500",
                        description = "Error interno",
                        content = @Content(schema = @Schema(example = "{\"code\":\"INTERNAL_ERROR\", \"message\":\"Unexpected error occurred\"}"))
                    )
                }
            )
        )
    })
    RouterFunction<ServerResponse> routerFunction(Handler handler, AuthHandler authHandler) {
        return RouterFunctions.nest(path("/api/v1"),
            route(POST("/usuarios"), handler::handleRegisterUser)
            .andRoute(POST("/login"), authHandler::handleLogin)
            .andRoute(GET("/usuarios/{id}/exists"), handler::handleGetUserById)
        );
    }
}
