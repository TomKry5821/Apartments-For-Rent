package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin;

import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.response.GetAllUsersResponse;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.user.UserEntity;

import java.util.Collection;

public interface AdminService {
    void deleteUser(Long userId);
    void deleteDbContent();
    GetAllUsersResponse getAllUsers();

}
