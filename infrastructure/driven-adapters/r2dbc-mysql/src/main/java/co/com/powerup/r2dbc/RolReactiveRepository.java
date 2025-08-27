package co.com.powerup.r2dbc;

import co.com.powerup.r2dbc.entities.RolEntity;
import co.com.powerup.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RolReactiveRepository extends ReactiveCrudRepository<RolEntity, Integer>, ReactiveQueryByExampleExecutor<RolEntity> {
    public Mono<Boolean> existsById(Long id);
}
