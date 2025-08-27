package co.com.powerup.api.dto;

import co.com.powerup.model.rol.Rol;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.time.LocalDate;


public record UserDTO (


        String id,
        @NotBlank(message = "Documento de identidad requerido")
        @NotNull
        String citizenId,
        @NotBlank(message = "Nombre requerido")
        @NotNull
        String name,
        @NotBlank(message = "Apellido requerido")
        @NotNull
        String lastName,
        LocalDate birthDate,
        String address,
        String phoneNumber,
        @NotBlank(message = "Email requerido")
        @Email(message = "Formato de email incorrecto")
        @NotNull
        String email,
        @DecimalMin(value = "0",inclusive = true,message = "El salario debe ser mayor a 0")
        @DecimalMax(value = "150000000",inclusive = true,message = "El salario debe ser inferior a 150000000")
        BigDecimal baseSalary,
        @NotNull
        @Positive
        Long idRol
) {
}
