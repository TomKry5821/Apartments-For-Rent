package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.request.ChangeUserDetailsRequest;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.userdetails.response.ChangeUserDetailsResponse;

import java.util.Collection;

public interface AdminService {
    Collection<UserDTO> getAllUsers();
    void deleteUser(Long userId);
    ChangeUserDetailsResponse changeUserDetails(ChangeUserDetailsRequest changeUserDetailsRequest, Long userId);
}
