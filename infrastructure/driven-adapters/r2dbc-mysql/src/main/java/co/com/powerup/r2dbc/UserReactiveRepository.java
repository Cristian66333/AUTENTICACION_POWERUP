package co.com.powerup.r2dbc;

import co.com.powerup.model.user.User;
import co.com.powerup.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {


    Mono<Boolean> existsByEmail(String email);

    Mono<User> findUserById(String id);

    Mono<User> findUserByEmail(String email);




}
