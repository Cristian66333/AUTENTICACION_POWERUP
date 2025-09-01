package co.com.powerup.model.user;
import co.com.powerup.model.rol.Rol;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;
    private String citizenId;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private BigDecimal baseSalary;
    private Long idRol;

}
