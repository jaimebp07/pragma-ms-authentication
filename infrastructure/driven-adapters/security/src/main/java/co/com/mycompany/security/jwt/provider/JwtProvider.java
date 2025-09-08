package co.com.mycompany.security.jwt.provider;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import co.com.mycompany.model.authentication.TokenService;
import co.com.mycompany.model.user.User;
import co.com.mycompany.security.jwt.extractor.JwtRoleExtractor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;  

/**
 * Servicio JWT que implementa TokenService.
 * Genera y valida tokens de forma reactiva en el microservicio de autenticación.
 */
//@Component
public class JwtProvider implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProvider.class);
    private final JwtRoleExtractor roleExtractor;

    @Value("${jwt.secret}")  
    private String secret;  
    @Value("${jwt.expiration}")  
    private Integer expiration;  

    public JwtProvider(JwtRoleExtractor roleExtractor) {
        this.roleExtractor = roleExtractor;
    }

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiration);

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(getKey(secret))
                .compact();
    }

    public List<String> getRoles(String token) {
        Claims claims = getClaims(token);
        return roleExtractor.extractRoles(claims);
    }

    private SecretKey getKey(String secret) {  
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);  
        return Keys.hmacShaKeyFor(secretBytes);  
    }  

    public boolean validate(String token) {
    try {
        getClaims(token);
        return true;
    } catch (JwtException | IllegalArgumentException e) {
        LOGGER.warn(" Token inválido: {}", e.getMessage());
        return false;
    }
}

    public Claims getClaims(String token) {  
        return Jwts.parser()  
                .verifyWith(getKey(secret))  
                .build()  
                .parseSignedClaims(token)  
                .getPayload();  
    }
}
