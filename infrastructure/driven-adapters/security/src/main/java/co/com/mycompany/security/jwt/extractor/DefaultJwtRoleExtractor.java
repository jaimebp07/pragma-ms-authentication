package co.com.mycompany.security.jwt.extractor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import io.jsonwebtoken.Claims;

//@Component
public class DefaultJwtRoleExtractor implements JwtRoleExtractor {

    @Override
    public List<String> extractRoles(Claims claims) {
        Object rolesObj = claims.get("roles");

        if (rolesObj == null) {
            return List.of();
        }

        if (rolesObj instanceof String) {
            return Stream.of(((String) rolesObj).split(","))
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .toList();
        }

        if (rolesObj instanceof Collection<?>) {
            return ((Collection<?>) rolesObj).stream()
                    .map(Object::toString)
                    .toList();
        }

        throw new IllegalArgumentException("Unsupported roles format: " + rolesObj.getClass());
    }

}
