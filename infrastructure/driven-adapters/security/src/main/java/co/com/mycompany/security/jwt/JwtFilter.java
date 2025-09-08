package co.com.mycompany.security.jwt;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;  
import org.springframework.web.server.WebFilter;  
import org.springframework.web.server.WebFilterChain;

import co.com.mycompany.security.jwt.provider.JwtProvider;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private static final List<String> EXCLUDED_PATHS = List.of("/api/v1/login");

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No se encontró token válido en la petición a {}", path);
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7);

        try {
            jwtProvider.validate(token);
            exchange.getAttributes().put("token", token);
            return chain.filter(exchange);
        } catch (Exception e) {
            log.warn("Token inválido en {}: {}", path, e.getMessage());
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired JWT token"));
        }
    }

}
