package co.com.powerup.api.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final javax.crypto.SecretKey SECRET =
            io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                    "clave-super-secreta-para-jwt-minimo-32-bytes!!".getBytes());
    @Bean
    SecurityWebFilterChain security(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios").hasAnyRole("ADMIN","ASESOR")
                        .pathMatchers(HttpMethod.GET, "/api/v1/usuarios/**").hasAnyRole("ADMIN","ASESOR","CLIENTE")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                )
                .build();
    }
    // Decodificador HS256 (mismo secret que firmas)
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(SECRET).build();
    }

    // Tomar authorities desde claim "roles" que ya trae "ROLE_*"
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter ga = new JwtGrantedAuthoritiesConverter();
        ga.setAuthoritiesClaimName("roles");
        ga.setAuthorityPrefix("");

        return jwt -> {
            var authorities = ga.convert(jwt);
            return Mono.just(new JwtAuthenticationToken(jwt, authorities, jwt.getSubject()));
        };
    }
}

