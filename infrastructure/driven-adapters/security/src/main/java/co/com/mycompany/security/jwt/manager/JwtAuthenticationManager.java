package co.com.mycompany.security.jwt.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import co.com.mycompany.security.jwt.provider.JwtProvider;
import reactor.core.publisher.Mono;

//@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationManager.class);

    private final JwtProvider jwtProvider;  
  
    public JwtAuthenticationManager(JwtProvider jwtProvider) {  
        this.jwtProvider = jwtProvider;  
    } 

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        if (authentication == null || authentication.getCredentials() == null) {
            // Se utiliza si la ausencia de credenciales no es error yse uiero probar otro AuthenticationManager
            //return Mono.empty(); 
            return Mono.error(new BadCredentialsException("Missing authentication credentials")); 
        }


        final String token = authentication.getCredentials().toString();

        // Mono.defer --> ejecuta cuando se suscriben, y las excepciones entran al flujo, donde puedes transformarlas
        return Mono.defer(() -> {
                String subject = jwtProvider.getClaims(token).getSubject();
                List<String> roles = jwtProvider.getRoles(token);

                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                return Mono.just(new UsernamePasswordAuthenticationToken(
                        subject,
                        null,
                        authorities
                ));
            })
            .cast(Authentication.class)
            .onErrorMap(ex -> {
                log.warn("Error de autenticación con JWT: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
                return new BadCredentialsException("Invalid or expired JWT token", ex);
            }
        );
    }
}
