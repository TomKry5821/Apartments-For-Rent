package pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.response;

import lombok.Data;
import pl.polsl.krypczyk.apartmentsforrent.userservice.domain.admin.dto.UserDTO;

import java.util.Collection;

@Data
public class GetAllUsersResponse {

    private Collection<UserDTO> users;
}
