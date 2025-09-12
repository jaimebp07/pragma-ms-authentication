package co.com.mycompany.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import co.com.mycompany.api.config.TestSecurityConfig;
import co.com.mycompany.api.dto.UserDTO;
import reactor.core.publisher.Mono;


@ContextConfiguration(classes = {
    RouterRest.class,
    Handler.class,
    AuthHandler.class,
    TestSecurityConfig.class    // <--- agregar
})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private Handler handler;

    @MockitoBean
    private  AuthHandler authHandler;

    @Test
    void testRegisterUser() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");

        UserDTO userDto = new UserDTO(
            "Andres",
            "Lopez",
            LocalDate.of(1990, 1, 1),
            "Calle 123",
            "3001234567",
            "test@example.com",
            BigDecimal.valueOf(3000),
            roles,
            "12345678"
        );

        when(handler.handleRegisterUser(any()))
                .thenReturn(
                    ServerResponse
                        .created(URI.create("/api/v1/usuarios"))
                        .body(Mono.just(userDto), UserDTO.class)
                    );

        webTestClient.post()
                .uri("/api/v1/usuarios")
                .bodyValue(userDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                        UserDTO body = response.getResponseBody();
                        assertNotNull(body);
                        assertEquals("test@example.com", body.email());
                        assertEquals("Andres", body.firstName());
                    }
                );
    }

}
