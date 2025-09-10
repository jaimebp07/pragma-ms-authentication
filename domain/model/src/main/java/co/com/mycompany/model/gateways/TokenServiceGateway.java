package co.com.mycompany.model.gateways;

import co.com.mycompany.model.user.User;

public interface TokenServiceGateway {
    String generateToken(User user);
}
