package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.dto.CreateUserRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsEntity;

@Mapper
public interface UserDetailsDTOUserDetailsEntityMapper {

    UserDetailsEntity userDetailsDTOToUserDetailsEntity(CreateUserRequestDTO createUserRequestDTO);
    CreateUserRequestDTO userDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);

}
