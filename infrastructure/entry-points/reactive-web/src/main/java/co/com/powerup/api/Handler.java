package co.com.powerup.api;


import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.api.mappers.UserMapper;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final Validator validator;
    private final Logger log = LoggerFactory.getLogger(Handler.class);

    public Mono<ServerResponse> listenPostSaveUser(ServerRequest serverRequest) {
        log.info("[LISTEN POST SAVE USER REQUEST]");
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(this::validate)
                .map(UserMapper::toDomain)
                .flatMap(userUseCase::saveUser)
                .flatMap(savedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .doOnSuccess(response -> log.info("A user was saved successfully"))
                .doOnError(e -> log.error("Save user error: " + e.getMessage()));//TODO logs: log?
                //.onErrorResume(Exception.class,error->ServerResponse.badRequest().bodyValue(error.getMessage()));// TODO excepciones?

    }
    private <T> Mono<T> validate(T body) {
        var violations = validator.validate(body);
        if (!violations.isEmpty()) return Mono.error(new ConstraintViolationException(violations));
        return Mono.just(body);
    }


    /*public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }*/
}
