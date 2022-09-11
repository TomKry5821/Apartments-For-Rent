package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.UUID;

public interface UserService {

    UserDetailsDTO getUserDetails(UUID accessToken);
}
