package co.com.mycompany.model.authentication;

import co.com.mycompany.model.user.User;

public interface TokenService {
    String generateToken(User user);
}
