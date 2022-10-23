package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    UserDetailsEntity userDetailsDTOToUserDetailsEntity(CreateUserRequest createUserRequest);
    GetUserDetailsResponse UserDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);
    ChangeUserDetailsResponse ChangeUserDetailsRequestToChangeUserDetailsResponse(ChangeUserDetailsRequest changeUserDetailsRequest);

}
