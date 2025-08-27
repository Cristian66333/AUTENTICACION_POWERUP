package co.com.powerup.usecase.user;

import co.com.powerup.model.rol.gateways.RolRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.user.exceptions.RolExistsException;
import co.com.powerup.usecase.user.exceptions.UserExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;

    public Mono<User> saveUser(User user) {

        return userRepository.existsByEmail(user.getEmail())
                .filter(exists -> !exists) // pasa solo si NO existe el email
                .switchIfEmpty(Mono.error(new UserExistsException("User already exists")))
                .then(
                        rolRepository.existsById(user.getIdRol())
                                .filter(Boolean::booleanValue) // pasa solo si SÍ existe el rol
                                .switchIfEmpty(Mono.error(new RolExistsException("Role does not exist")))
                )
                .then(userRepository.saveUser(user));
    }

}
