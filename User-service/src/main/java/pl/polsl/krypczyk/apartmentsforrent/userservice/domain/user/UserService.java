package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.GetUserDetailsResponse;

public interface UserService {

    GetUserDetailsResponse getUserDetails(Long userId);
    ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId);
    void inactivateAccount(Long userId);
}
