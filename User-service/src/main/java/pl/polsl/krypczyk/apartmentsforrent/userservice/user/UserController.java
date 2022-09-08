package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.dto.UserCreatedResponseDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.userdetails.UserDetailsDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/")
public class UserController {

    private final UserService userService;
}
