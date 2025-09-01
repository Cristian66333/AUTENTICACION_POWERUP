package co.com.powerup.model.user.gateways;

public interface PasswordHasher {
    String hash(String raw);
    boolean matches(String raw, String hashed);
}
