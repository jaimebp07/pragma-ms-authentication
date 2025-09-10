package co.com.mycompany.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import co.com.mycompany.security.repository.SecurityContextRepository;
import reactor.core.publisher.Mono; 

@Configuration
@EnableWebFluxSecurity  // Habilita la seguridad web reactiva
@EnableReactiveMethodSecurity // Permite utilizar anotaciones como @PreAuthorize, @PostAuthorize, @PreFilter, y @PostFilter en métodos
public class SecurityConfig {  
  
    private final SecurityContextRepository securityContextRepository;  
  
    public SecurityConfig(SecurityContextRepository securityContextRepository) {  
        this.securityContextRepository = securityContextRepository;  
    }  
  
    @Bean  
    SecurityWebFilterChain filterChain(ServerHttpSecurity http) {  
        return http  
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers("/api/v1/login**").permitAll()
                        .pathMatchers("/api/v1/usuarios/*/exists").permitAll()
                        .pathMatchers(
                                "/actuator/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/v3/api-docs/swagger-config"
                            ).permitAll() 
                        .anyExchange().authenticated())   
                .securityContextRepository(securityContextRepository)  
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)  
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)  
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .exceptionHandling(ex -> ex 
                        .authenticationEntryPoint((swe, e) ->  // Que hacer un cliente no autorizado intenta acceder
                            Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((swe, e) ->  // Que hacer un cliente autorizado intenta acceder a un recurso prohibido
                            Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                )
                .build();  
    } 
}