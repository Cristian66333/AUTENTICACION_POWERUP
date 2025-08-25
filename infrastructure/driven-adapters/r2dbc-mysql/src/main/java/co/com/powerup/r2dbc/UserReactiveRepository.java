package co.com.powerup.r2dbc;

import co.com.powerup.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {
    public Mono<UserEntity> findUserByEmail(String email);

    public Mono<Boolean> existsByIdAndEmail(String idNumber, String email);

}
