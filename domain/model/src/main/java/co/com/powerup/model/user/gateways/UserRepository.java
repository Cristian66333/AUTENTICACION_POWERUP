package co.com.powerup.model.user.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Mono<Boolean> existsByEmail(String email);

    Mono<User> findUserById(String id);

    Mono<User> findUserByEmail(String email);

}
