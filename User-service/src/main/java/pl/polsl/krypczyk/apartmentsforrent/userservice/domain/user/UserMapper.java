package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import org.mapstruct.Mapper;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.userdetails.UserDetailsEntity;

@Mapper
public interface UserMapper {

    UserDetailsEntity userDetailsDTOToUserDetailsEntity(CreateUserRequest createUserRequest);
    GetUserDetailsResponse UserDetailsEntityToUserDetailsDTO(UserDetailsEntity userDetailsEntity);
    ChangeUserDetailsResponse ChangeUserDetailsRequestToChangeUserDetailsResponse(ChangeUserDetailsRequest changeUserDetailsRequest);

}
