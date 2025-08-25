package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {

        return userRepository.existsByIdAndEmail(user.getId(), user.getEmail())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new Exception("User already exists")))
                .then(userRepository.saveUser(user));
    }

}
