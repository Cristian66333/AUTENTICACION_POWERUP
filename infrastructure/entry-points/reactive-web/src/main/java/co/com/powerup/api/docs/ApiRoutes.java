package co.com.powerup.api.docs;


import co.com.powerup.api.Handler;
import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.api.errorHandler.ApiError; // si tienes este modelo para errores
import co.com.powerup.model.auth.LoginCommand;
import co.com.powerup.model.auth.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ApiRoutes {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    method = org.springframework.web.bind.annotation.RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPostSaveUser",
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Crear usuarios",
                            description = "Crea un usuario validando DTO y reglas de negocio.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = UserDTO.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Usuario creado",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = UserDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación / JSON inválido",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiError.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Duplicado (email/documento)",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiError.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "FK inválida (id_rol no existe)",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiError.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error inesperado",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiError.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRouter(Handler handler) {
        return route(POST("/api/v1/usuarios").and(accept(MediaType.APPLICATION_JSON)), handler::listenPostSaveUser);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    method = org.springframework.web.bind.annotation.RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenPostLogin",
                    operation = @Operation(
                            operationId = "login",
                            summary = "Inicio de sesión usuarios",
                            description = "Inicia sesión de usuario a partir del rol y devuelve un JWT",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = LoginCommand.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Inicio de sesión exitoso",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = TokenResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Credenciales incorrectas",
                                            content = @Content(mediaType = "application/json",
                                                    schema = @Schema(implementation = ApiError.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> login(Handler handler) {
        return route(POST("/api/v1/login").and(accept(MediaType.APPLICATION_JSON)), handler::listenPostLogin);
    }
}
