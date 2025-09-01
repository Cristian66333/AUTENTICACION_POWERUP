package co.com.powerup.usecase.user;

import co.com.powerup.model.rol.gateways.RolRepository;
import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.PasswordHasher;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.user.exceptions.RolExistsException;
import co.com.powerup.usecase.user.exceptions.UserExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordHasher passwordHasher;

    public Mono<User> saveUser(User user) {

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> exists
                        ? Mono.error(new UserExistsException("User already exists"))
                        : Mono.empty()
                )
                .then(
                        rolRepository.existsById(user.getIdRol())
                                .flatMap(exists -> exists
                                        ? Mono.<Void>empty()
                                        : Mono.error(new RolExistsException("Role does not exist"))
                                )
                )
                .then(Mono.fromCallable(() -> passwordHasher.hash(user.getPassword()))
                        .subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic())
                        .map(hashed -> {
                            user.setPassword(hashed);
                            return user;
                        })
                )
                .flatMap(userRepository::saveUser)
                .map(saved -> {
                    saved.setPassword(null);
                    return saved;
                });
    }

    public Mono<User> findUserById(String id) {
        return userRepository.findUserById(id)
                .switchIfEmpty(Mono.error(new UserExistsException("User does not exist")));
    }


}
