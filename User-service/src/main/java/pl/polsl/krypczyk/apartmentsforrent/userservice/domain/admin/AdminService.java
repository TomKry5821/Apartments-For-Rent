package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.authorization.userdetails.response.ChangeUserDetailsResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.authorization.exception.InactiveAccountException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.InvalidUserDetailsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.exception.UserNotFoundException;

import java.util.Collection;

public interface AdminService {
    Collection<UserDTO> getAllUsers();
    void deleteUser(Long userId) throws UserNotFoundException;
    ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId) throws InvalidUserDetailsException, UserNotFoundException, InactiveAccountException;
    void activateAccount(Long userId) throws UserNotFoundException;
}
