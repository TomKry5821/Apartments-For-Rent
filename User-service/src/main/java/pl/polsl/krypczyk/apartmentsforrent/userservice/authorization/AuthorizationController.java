package pl.polsl.krypczyk.apartmentsforrent.userservice.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.annotation.uuid;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoggedInResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserLoginRequestDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.CreateUserRequestDTO;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreatedResponseDTO registerNewUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        return this.authorizationService.registerNewUser(createUserRequestDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoggedInResponseDTO loginUser(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        return this.authorizationService.loginUser(userLoginRequestDTO);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logoutUser(@uuid(message = "Invalid access token") @RequestHeader("Authorization") UUID accessToken){
        this.authorizationService.logoutUser(accessToken);
    }

}
