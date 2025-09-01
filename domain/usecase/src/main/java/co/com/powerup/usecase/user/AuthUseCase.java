package co.com.powerup.usecase.user;

import co.com.powerup.model.auth.LoginCommand;
import co.com.powerup.model.auth.TokenResponse;
import co.com.powerup.model.auth.gateways.TokenProvider;
import co.com.powerup.model.rol.gateways.RolRepository;
import co.com.powerup.model.user.gateways.PasswordHasher;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository users;
    private final RolRepository roles;
    private final PasswordHasher hasher;
    private final TokenProvider tokenProvider;

    public Mono<TokenResponse> login(LoginCommand cmd) {
        final String email = cmd.getEmail().trim().toLowerCase();

        return users.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario/clave inválidos")))
                .flatMap(u -> hasher.matches(cmd.getPassword(), u.getPassword())
                        ? Mono.just(u)
                        : Mono.error(new RuntimeException("Usuario/clave inválidos")))
                .flatMap(u ->
                        roles.findById(u.getIdRol())
                                .switchIfEmpty(Mono.error(new RuntimeException("Rol no encontrado")))
                                .map(rol -> {
                                    // Construimos authority estilo Spring Security
                                    String authority = "ROLE_" + rol.getName().toUpperCase(); // ej: ROLE_CLIENTE
                                    String jwt = tokenProvider.generate(u.getId(), u.getEmail(), java.util.List.of(authority), ""+u.getIdRol());
                                    return new TokenResponse("Bearer", jwt, tokenProvider.expiresInSeconds());
                                })
                );
    }
}
