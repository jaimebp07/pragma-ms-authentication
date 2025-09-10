package co.com.mycompany.model.gateways;

public interface PasswordServiceGateway {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
