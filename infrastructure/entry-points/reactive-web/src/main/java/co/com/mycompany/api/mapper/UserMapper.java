package co.com.mycompany.api.mapper;

import org.mapstruct.Mapper;

import co.com.mycompany.api.dto.UserDTO;
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
                .build();
    }

    UserDTO toDTO(User domain);

}
