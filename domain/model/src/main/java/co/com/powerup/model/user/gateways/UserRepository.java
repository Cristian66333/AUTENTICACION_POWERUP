package co.com.powerup.model.user.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> saveUser(User user);

    Mono<User> findUserByID(String idNumber);

    Mono<Boolean> existsByIdAndEmail(String idNumber, String email);
}
