package co.com.mycompany.api.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import co.com.mycompany.api.dto.CustomerRsDTO;
import co.com.mycompany.model.user.User;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerRsDTO toCustomerDto(User user);

    Set<CustomerRsDTO> toCustomerDtoSet(Set<User> users);
    
}
