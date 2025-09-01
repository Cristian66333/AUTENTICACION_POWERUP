package co.com.powerup.r2dbc.jwt;

import co.com.powerup.model.auth.gateways.TokenProvider;
import org.springframework.stereotype.Component;

@Component
public class JwtProviderAdapter implements TokenProvider {
    private static final long EXP = 3600L;
    private final javax.crypto.SecretKey key = io.jsonwebtoken.security.Keys
            .hmacShaKeyFor("clave-super-secreta-para-jwt-minimo-32-bytes!!".getBytes());

    @Override
    public String generate(String userId, String email, java.util.List<String> roles, String roleId) {
        java.time.Instant now = java.time.Instant.now();
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId)
                .claim("email", email)
                .claim("roles", roles)     // ["ROLE_CLIENTE"] para que solicitudes pueda hacer hasRole("CLIENTE")
                .claim("role_id", roleId)  // opcional, útil para auditoría
                .setIssuedAt(java.util.Date.from(now))
                .setExpiration(java.util.Date.from(now.plusSeconds(EXP)))
                .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }
    @Override
    public long expiresInSeconds() {
        return EXP;
    }
}
