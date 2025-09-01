package co.com.powerup.r2dbc.encoder;

import co.com.powerup.model.user.gateways.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public String hash(String raw) {
        return encoder.encode(raw);
    }
    @Override
    public boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }
}
