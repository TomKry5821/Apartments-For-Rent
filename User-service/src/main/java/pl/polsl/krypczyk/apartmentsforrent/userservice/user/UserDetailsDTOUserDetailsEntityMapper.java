package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsEntity;

@Mapper
public interface UserDetailsDTOUserDetailsEntityMapper {

    UserDetailsEntity userDetailsDTOToUserDetailsEntity(UserDetailsDTO userDetailsDTO);
    UserDetailsDTO userDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);

}
