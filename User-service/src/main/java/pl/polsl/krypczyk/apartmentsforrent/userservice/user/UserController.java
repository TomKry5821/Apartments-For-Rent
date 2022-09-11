package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.polsl.krypczyk.apartmentsforrent.userservice.annotation.uuid;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.UUID;

@RestController
@RequestMapping("user/api/v1/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("users/details")
    public UserDetailsDTO getUserDetails(@RequestHeader("Authorization") @uuid UUID accessToken){
        return this.userService.getUserDetails(accessToken);
    }
}

