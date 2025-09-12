package co.com.mycompany.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import co.com.mycompany.api.dto.UserDTO;
import co.com.mycompany.model.authentication.Role;
import co.com.mycompany.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toDomain(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        return new User.Builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .birthDate(dto.birthDate())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .baseSalary(dto.baseSalary())
                .roles(dto.roles() == null ? List.of() :
                    dto.roles().stream().map(rol -> Role.valueOf(rol.toUpperCase())).toList()
                )
                .password(dto.password())
                .build();
    }

    UserDTO toDTO(User domain);

}
