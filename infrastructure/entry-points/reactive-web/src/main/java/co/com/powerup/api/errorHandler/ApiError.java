package co.com.powerup.api.errorHandler;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ApiError", description = "Formato estándar de error")
public record ApiError(
        @Schema(example = "/users") String path,
        @Schema(example = "400") int status,
        @Schema(example = "Bad Request") String error,
        @Schema(example = "Errores de validación") String message,
        List<FieldViolation> violations,
        OffsetDateTime timestamp
) {
    public static record FieldViolation(
            @Schema(example = "email") String field,
            @Schema(example = "no-es-email") String rejectedValue,
            @Schema(example = "Formato de email incorrecto") String message
    ) {}
}

