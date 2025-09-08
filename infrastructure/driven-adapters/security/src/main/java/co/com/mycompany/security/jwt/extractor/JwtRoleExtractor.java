package co.com.mycompany.security.jwt.extractor;

import java.util.List;

import io.jsonwebtoken.Claims;

public interface JwtRoleExtractor {
    List<String> extractRoles(Claims claims);
}
