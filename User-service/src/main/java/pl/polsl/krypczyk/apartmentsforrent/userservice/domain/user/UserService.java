package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.dto.response.GetUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.request.CreateUserRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.user.dto.response.CreateUserResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

public interface UserService {

    CreateUserResponse createUser(CreateUserRequest createUserRequest) throws UserAlreadyExistsException;

    GetUserDetailsResponse getUserDetails(Long userId) throws UserNotFoundException;

    ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws InvalidUserDetailsException, UserNotFoundException, UserAlreadyExistsException;

    void inactivateAccount(Long userId) throws UserNotFoundException;
}
