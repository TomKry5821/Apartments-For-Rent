package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin;

import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.response.GetAllUsersResponse;

public interface AdminService {
    void deleteUser(Long userId);
    void deleteDbContent();
    GetAllUsersResponse getAllUsers();

}
