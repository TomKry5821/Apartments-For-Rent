package pl.polsl.krypczyk.apartmentsforrent.userservice.user.mapper;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

@Mapper
public interface UserMapper {

    UserDetailsEntity userDetailsDTOToUserDetailsEntity(CreateUserRequestDTO createUserRequestDTO);
    CreateUserRequestDTO userDetailsEntityToCreateUserRequestDTO(UserDetailsEntity userDetailsEntity);
    UserDetailsDTO UserDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);

}
