package pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.response;

import lombok.Data;
import pl.polsl.krypczyk.apartmentsforrent.userservice.application.admin.dto.UserDTO;

import java.util.Collection;

@Data
public class GetAllUsersResponse {

    private Collection<UserDTO> users;
}
