package co.com.mycompany.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.com.mycompany.security.jwt.extractor.DefaultJwtRoleExtractor;
import co.com.mycompany.security.jwt.extractor.JwtRoleExtractor;
import co.com.mycompany.security.jwt.provider.JwtProvider;
import co.com.mycompany.security.jwt.manager.JwtAuthenticationManager;
import co.com.mycompany.security.repository.SecurityContextRepository;

@Configuration
public class SecurityBeansConfig {

    @Bean
    JwtRoleExtractor jwtRoleExtractor() {
        return new DefaultJwtRoleExtractor();
    }

    @Bean
    JwtProvider jwtProvider(JwtRoleExtractor jwtRoleExtractor) {
        return new JwtProvider(jwtRoleExtractor);
    }

    @Bean
    JwtAuthenticationManager jwtAuthenticationManager(JwtProvider jwtProvider) {
        return new JwtAuthenticationManager(jwtProvider);
    }

    @Bean
    SecurityContextRepository securityContextRepository(JwtAuthenticationManager jwtAuthenticationManager) {
        return new SecurityContextRepository(jwtAuthenticationManager);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}