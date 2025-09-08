package co.com.mycompany.security.repository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpHeaders;

import co.com.mycompany.security.jwt.manager.JwtAuthenticationManager;
import reactor.core.publisher.Mono;

//@Repository
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager jwtAuthenticationManager;  
  
    public SecurityContextRepository(JwtAuthenticationManager jwtAuthenticationManager) {  
        this.jwtAuthenticationManager = jwtAuthenticationManager;  
    }  
  
    @Override  
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {  
        return Mono.empty();  
    }  
  
    @Override  
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty(); 
        }

        String token = authHeader.substring(7);

        return jwtAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(null, token))
                .map(SecurityContextImpl::new);
    }
}
