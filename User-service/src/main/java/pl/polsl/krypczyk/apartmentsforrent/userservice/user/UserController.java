package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.krypczyk.apartmentsforrent.userservice.annotation.uuid;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.UUID;

@RestController
@RequestMapping("user/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{accessToken}")
    public UserDetailsDTO getUserDetails(@PathVariable("accessToken") @uuid UUID accessToken){
        return this.userService.getUserDetails(accessToken);
    }
}

