package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoggedInResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;

public interface AuthorizationService {

     UserCreatedResponseDTO registerNewUser(UserDetailsDTO userDetailsDTO) throws BadCredentialsException, UserAlreadyExistsException;
     UserLoggedInResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) throws UserNotFoundException, BadCredentialsException;
}
