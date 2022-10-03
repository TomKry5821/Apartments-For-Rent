package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

public interface UserService {

    GetUserDetailsResponse getUserDetails(Long userId) throws UserNotFoundException;

    ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws InvalidUserDetailsException, UserNotFoundException, UserAlreadyExistsException;

    void inactivateAccount(Long userId) throws UserNotFoundException;
}
