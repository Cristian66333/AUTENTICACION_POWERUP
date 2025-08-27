package co.com.powerup.api.errorHandler;

import jakarta.validation.ConstraintViolation;
import org.springframework.web.server.ServerWebExchange;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public final class ErrorMapper {
    private ErrorMapper(){}

    public static ApiError fromViolations(Set<ConstraintViolation<?>> violations, ServerWebExchange exchange) {
        List<ApiError.FieldViolation> fields = violations.stream()
                .map(v -> new ApiError.FieldViolation(
                        v.getPropertyPath().toString(),
                        v.getInvalidValue() == null ? null : v.getInvalidValue().toString(),
                        v.getMessage()))
                .toList();

        return new ApiError(
                exchange.getRequest().getPath().value(),
                400,
                "Bad Request",
                "Errores de validación",
                fields,
                OffsetDateTime.now()
        );
    }

    public static ApiError simple(int status, String error, String message, ServerWebExchange exchange) {
        return new ApiError(
                exchange.getRequest().getPath().value(),
                status,
                error,
                message,
                List.of(),
                OffsetDateTime.now()
        );
    }
}
