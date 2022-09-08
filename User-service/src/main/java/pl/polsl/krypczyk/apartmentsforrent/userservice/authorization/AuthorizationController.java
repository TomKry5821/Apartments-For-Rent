package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.BadCredentialsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserAlreadyExistsException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.exception.UserNotFoundException;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoggedInResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;

@RestController
@RequestMapping("/user/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponseDTO registerNewUser(@RequestBody UserDetailsDTO userDetailsDTO) throws BadCredentialsException, UserAlreadyExistsException {
        return this.authorizationService.registerNewUser(userDetailsDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoggedInResponseDTO loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO) throws UserNotFoundException, BadCredentialsException {
        return this.authorizationService.loginUser(userLoginRequestDTO);
    }

}
