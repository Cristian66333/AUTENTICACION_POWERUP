package co.com.powerup.api.errorHandler;


import co.com.powerup.usecase.user.exceptions.RolExistsException;
import co.com.powerup.usecase.user.exceptions.UserExistsException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.codec.DecodingException;

@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ApiError body;
        HttpStatus status;

        if (ex instanceof ConstraintViolationException cve) {
            status = HttpStatus.BAD_REQUEST;
            body = ErrorMapper.fromViolations(cve.getConstraintViolations(), exchange);
        } else if (ex instanceof DecodingException) {
            status = HttpStatus.BAD_REQUEST;
            body = ErrorMapper.simple(400, "Bad Request", "JSON mal formado o tipos inválidos", exchange);
        } else if (ex instanceof ResponseStatusException rse) {
            status = HttpStatus.valueOf(rse.getStatusCode().value());
            body = ErrorMapper.simple(status.value(), status.toString(), rse.getReason(), exchange);
        } else if (ex instanceof UserExistsException use){
            status = HttpStatus.BAD_REQUEST;
            body = ErrorMapper.simple(400, "Bad Request", use.getMessage(), exchange);
        }else if (ex instanceof RolExistsException roe) {
            status = HttpStatus.BAD_REQUEST;
            body = ErrorMapper.simple(400, "Bad Request", roe.getMessage(), exchange);
        }else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = ErrorMapper.simple(500, "Internal Server Error", "Ha ocurrido un error inesperado", exchange);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = mapper.writeValueAsBytes(body);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception writeErr) {
            return exchange.getResponse().setComplete();
        }
    }
}
