package co.com.powerup.model.auth.gateways;

public interface TokenProvider {
    String generate(String userId, String email, java.util.List<String> roles, String roleId);
    long expiresInSeconds();
}
