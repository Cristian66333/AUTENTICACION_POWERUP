package co.com.powerup.api.mappers;


import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.model.rol.Rol;
import co.com.powerup.model.user.User;
import org.hibernate.validator.constraints.UUID;
import org.springframework.stereotype.Component;


public class UserMapper {
    public static User toDomain(UserDTO userDTO){
        return User.builder()
                .id(userDTO.id())
                .citizenId(userDTO.citizenId())
                .email(userDTO.email())
                .password(userDTO.password())
                .name(userDTO.name())
                .lastName(userDTO.lastName())
                .address(userDTO.address())
                .birthDate(userDTO.birthDate())
                .baseSalary(userDTO.baseSalary())
                .phoneNumber(String.valueOf(userDTO.phoneNumber()))
                .idRol(userDTO.idRol())
                .build();
    }

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        String id = null;
        if (user.getId() != null) {
            id = user.getId();
        }

        Long idRol = null;

        if (user.getIdRol() != null) {
            idRol = user.getIdRol();
        }


        return new UserDTO(
                id,
                user.getCitizenId(),
                user.getName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPassword(),
                user.getBaseSalary(),
                idRol
        );
    }
}
