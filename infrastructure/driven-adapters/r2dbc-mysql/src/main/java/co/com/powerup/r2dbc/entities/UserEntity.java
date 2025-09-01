package co.com.powerup.r2dbc.entities;




import co.com.powerup.model.rol.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
    @Column("documento_identidad")
    private String citizenId;
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
    @Column("password")
    private String password;
    @Column("salario_base")
    private BigDecimal baseSalary;
    @Column("id_rol")
    private Long idRol;
}
