package co.com.powerup.api.auth;

import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.model.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(
            "clave-super-secreta-para-jwt-minimo-32-bytes!!".getBytes());

    public String generate(UserDTO u) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(u.id())
                .claim("user_id", u.id())
                .claim("email", u.email())
                .claim("roles", u.idRol()) // ["ROLE_ADMIN", "ROLE_ASESOR", "ROLE_CLIENTE"]
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
