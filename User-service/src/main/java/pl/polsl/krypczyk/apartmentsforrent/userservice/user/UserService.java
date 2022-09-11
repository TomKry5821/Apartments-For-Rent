package pl.polsl.krypczyk.apartmentsforrent.userservice.user;

import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.ChangeUserDetailsDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.user.userdetails.dto.UserDetailsDTO;

import java.util.UUID;

public interface UserService {

    UserDetailsDTO getUserDetails(Long userId);
    ChangeUserDetailsDTO changeUserDetails(ChangeUserDetailsDTO changeUserDetailsDTO, Long userId);
    void deleteDbContent();
    void inactivateAccount(Long userId);
}
