package co.com.powerup.r2dbc.entities;




import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column("nombre")
    private String name;
    @Column("apellido")
    private String lastName;
    @Column("fecha_nacimiento")
    private LocalDate birthDate;
    @Column("direccion")
    private String address;
    @Column("numero_telefono")
    private String phoneNumber;
    @Column("correo")
    private String email;
    @Column("salario_base")
    private BigDecimal baseSalary;
}
