package pl.polsl.krypczyk.apartmentsforrent.userservice.user.mapper;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.UserDetailsEntity;

@Mapper
public interface UserDetailsDTOUserDetailsEntityMapper {

    UserDetailsEntity userDetailsDTOToUserDetailsEntity(CreateUserRequestDTO createUserRequestDTO);
    CreateUserRequestDTO userDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);

}
