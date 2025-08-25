package co.com.powerup.api.mappers;


import co.com.powerup.api.dto.UserDTO;
import co.com.powerup.model.user.User;
import org.springframework.stereotype.Component;


public class UserMapper {
    public static User toDomain(UserDTO userDTO){
        return User.builder()
                .id(userDTO.id())
                .email(userDTO.email())
                .name(userDTO.name())
                .lastName(userDTO.lastName())
                .address(userDTO.address())
                .birthDate(userDTO.birthDate())
                .baseSalary(userDTO.baseSalary())
                .phoneNumber(String.valueOf(userDTO.phoneNumber()))
                .build();
    }

    public static UserDTO toDTO(User user){
        if(user==null){
            return null;
        }else {
            return UserDTO.class.cast(user);
        }
    }
}
