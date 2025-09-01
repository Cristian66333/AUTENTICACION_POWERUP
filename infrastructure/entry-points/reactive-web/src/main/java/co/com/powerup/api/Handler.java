package co.com.powerup.api;


import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.api.mappers.UserMapper;
import co.com.powerup.model.auth.LoginCommand;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.user.AuthUseCase;
import co.com.powerup.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final AuthUseCase authUseCase;
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

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        log.info("[LISTEN GET USER BY ID REQUEST]");

        String id = serverRequest.pathVariable("id");
        return userUseCase.findUserById(id)
                .map(UserMapper::toDTO)
                .flatMap(user ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(user))
                .doOnSuccess(response -> log.info("A user was returned successfully"))
                .doOnError(e -> log.error("Get user by citizenId error not found: " + e.getMessage()));

    }
    public Mono<ServerResponse> listenPostLogin(ServerRequest req) {
        log.info("[LOGIN] inicio de sesión realizado...");
        return req.bodyToMono(LoginCommand.class)
                .flatMap(authUseCase::login)
                .flatMap(t -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(t))
                .onErrorResume(e -> {
                    log.warn("[LOGIN] failed: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(Map.of("error","unauthorized","message","Usuario/clave inválidos"));
                });
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
